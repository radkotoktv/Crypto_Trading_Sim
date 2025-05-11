document.addEventListener("DOMContentLoaded", () => {
    updateBalanceDisplay();

    document.getElementById('buy-button').addEventListener('click', () => {
        // Your existing buy logic...
        updateBalanceDisplay(); // Refresh balance after buy
    });

    document.getElementById('sell-button').addEventListener('click', () => {
        // Your existing sell logic...
        updateBalanceDisplay(); // Refresh balance after sell
    });

    const stompClient = new StompJs.Client({
        brokerURL: 'ws://localhost:8080/gs-guide-websocket'
    });

    stompClient.onConnect = (frame) => {
        console.log("Connected to STOMP: " + frame);
        stompClient.subscribe('/topic/price', (message) => {
            const priceData = JSON.parse(message.body);
            if (Array.isArray(priceData)) {
                updateTable(priceData);
            }
        });
    };

    stompClient.onWebSocketError = (error) => {
        console.error('WebSocket error:', error);
    };

    stompClient.onStompError = (frame) => {
        console.error('STOMP error:', frame.headers['message']);
        console.error('Details:', frame.body);
    };

    stompClient.activate();

    function updateTable(prices) {
        const tableBody = document.getElementById("price-table-body");
        const rows = tableBody.getElementsByTagName("tr");

        const existingSymbols = {};
        for (let row of rows) {
            const symbolCell = row.cells[0];
            existingSymbols[symbolCell.textContent.trim().toUpperCase()] = row;
        }

        prices.forEach(price => {
            const incomingSymbol = price.symbol.trim().toUpperCase();

            const date = new Date(price.timestamp);

            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            const seconds = String(date.getSeconds()).padStart(2, '0');
            const timeFormatted = `${hours}:${minutes}:${seconds}`;

            if (existingSymbols[incomingSymbol]) {
                const row = existingSymbols[incomingSymbol];
                row.cells[1].textContent = price.price;
                row.cells[2].textContent = timeFormatted;
            } else {
                if (rows.length >= 20) {
                    tableBody.deleteRow(0);
                }

                const newRow = document.createElement("tr");
                newRow.innerHTML = `
                    <td>${incomingSymbol}</td>
                    <td>${price.price}</td>
                    <td>${timeFormatted}</td>
                `;
                tableBody.appendChild(newRow);
            }
        });

        const sortedRows = Array.from(tableBody.getElementsByTagName("tr"))
            .sort((a, b) => {
                const priceA = parseFloat(a.cells[1].textContent);
                const priceB = parseFloat(b.cells[1].textContent);
                return priceA - priceB;
            });

        tableBody.innerHTML = "";
        sortedRows.reverse().forEach(row => tableBody.appendChild(row));
    }

    document.getElementById("triggerSqlButton").addEventListener("click", async () => {
        try {
            const response = await fetch('/api/assetpairs');

            if (response.ok) {
                document.getElementById("status").textContent = "SQL script triggered!";
                console.log("Backend executed the script (check server logs)");
            } else {
                throw new Error("Failed to trigger script");
            }
        } catch (error) {
            document.getElementById("status").textContent = "Error: " + error.message;
            console.error("Error:", error);
        }
    });
});

// Function to fetch and display balance
async function updateBalanceDisplay() {
    try {
        const user = JSON.parse(localStorage.getItem('currentUser'));
        if (!user || !user.token) {
            console.log("User not logged in");
            return;
        }

        const response = await fetch(`/api/balance/${user.id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${user.token || ''}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to fetch balance');
        }

        const balance = await response.json();
        document.getElementById("balance").innerHTML = `Current Balance: $${balance.toFixed(2)}`;
    } catch (error) {
        console.error('Balance update error:', error);
        document.getElementById("balance").innerHTML = 'Could not load balance';
    }
}