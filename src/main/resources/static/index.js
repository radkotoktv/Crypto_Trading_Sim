document.addEventListener("DOMContentLoaded", () => {
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