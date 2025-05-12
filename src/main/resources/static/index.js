document.addEventListener("DOMContentLoaded", () => {
    updateBalanceDisplay();

    document.getElementById('buy-button').addEventListener('click', async () => {
        try {
            const user = JSON.parse(localStorage.getItem('currentUser'));
            if (!user) {
                alert('Please log in first');
                return;
            }

            const cryptoSymbol = document.getElementById('crypto-symbol').value.trim().toUpperCase();
            const quantity = parseFloat(document.getElementById('quantity').value);

            if (!cryptoSymbol) {
                alert('Please enter a cryptocurrency symbol');
                return;
            }

            if (isNaN(quantity) || quantity <= 0) {
                alert('Please enter a valid quantity');
                return;
            }

            const tableBody = document.getElementById("price-table-body");
            const rows = tableBody.getElementsByTagName("tr");
            let unitPrice = null;
            let cryptoId = null;

            for (let row of rows) {
                if (row.cells[0].textContent.trim().toUpperCase() === cryptoSymbol) {
                    unitPrice = parseFloat(row.cells[1].textContent);
                    cryptoId = await getCryptoIdFromSymbol(cryptoSymbol);
                    break;
                }
            }

            if (!unitPrice) {
                alert(`Could not find current price for ${cryptoSymbol}`);
                return;
            }


            if (!cryptoId) {
                alert(`Could not find ID for ${cryptoSymbol}`);
                return;
            }

            const totalCost = quantity * unitPrice;

            const transactionData = {
                user_id: user.id,
                crypto_id: cryptoId,
                type: "buy",
                quantity: quantity,
                unit_price: unitPrice,
                total_cost: totalCost
            };

            console.log('Transaction data:', transactionData);

            const response = await fetch('/api/buy', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${user.token}`
                },
                body: JSON.stringify(transactionData)
            });

            if (!response.ok) {
                throw new Error(await response.text());
            }

            const result = await response.json();
            document.getElementById('transaction-status').textContent = 'Purchase successful!';
            console.log('Transaction created:', result);

            updateBalanceDisplay();

        } catch (error) {
            console.error('Transaction error:', error);
            document.getElementById('transaction-status').textContent = 'Error: ' + error.message;
        }
    });

    document.getElementById('sell-button').addEventListener("click", async () => {
        try {
            const user = JSON.parse(localStorage.getItem('currentUser'));
            if (!user) {
                alert('Please log in first');
                return;
            }

            const cryptoSymbol = document.getElementById('crypto-symbol').value.trim().toUpperCase();
            const quantity = parseFloat(document.getElementById('quantity').value);

            if (!cryptoSymbol) {
                alert('Please enter a cryptocurrency symbol');
                return;
            }

            if (isNaN(quantity) || quantity <= 0) {
                alert('Please enter a valid quantity');
                return;
            }

            const tableBody = document.getElementById("price-table-body");
            const rows = tableBody.getElementsByTagName("tr");
            let unitPrice = null;
            let cryptoId = null;

            for (let row of rows) {
                if (row.cells[0].textContent.trim().toUpperCase() === cryptoSymbol) {
                    unitPrice = parseFloat(row.cells[1].textContent);
                    cryptoId = await getCryptoIdFromSymbol(cryptoSymbol);
                    break;
                }
            }

            if (!unitPrice) {
                alert(`Could not find current price for ${cryptoSymbol}`);
                return;
            }


            if (!cryptoId) {
                alert(`Could not find ID for ${cryptoSymbol}`);
                return;
            }

            const totalCost = quantity * unitPrice;

            const transactionData = {
                user_id: user.id,
                crypto_id: cryptoId,
                type: "sell",
                quantity: quantity,
                unit_price: unitPrice,
                total_cost: totalCost
            };

            console.log('Transaction data:', transactionData);

            const response = await fetch('/api/sell', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${user.token}`
                },
                body: JSON.stringify(transactionData)
            });

            if (!response.ok) {
                throw new Error(await response.text());
            }

            const result = await response.json();
            document.getElementById('transaction-status').textContent = 'Sale successful!';
            console.log('Transaction created:', result);

            updateBalanceDisplay();

        } catch (error) {
            console.error('Transaction error:', error);
            document.getElementById('transaction-status').textContent = 'Error: ' + error.message;
        }
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
});

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

async function getCryptoIdFromSymbol(symbol) {
    try {
        const response = await fetch(`/api/id?name=${encodeURIComponent(symbol)}`);
        if (!response.ok) return null;
        return await response.json();
    } catch (error) {
        console.error('Error fetching crypto ID:', error);
        return null;
    }
}