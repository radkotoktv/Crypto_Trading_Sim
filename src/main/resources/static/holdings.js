document.addEventListener("DOMContentLoaded", () => {
    const backButton = document.getElementById("back-button");
    const holdingsBody = document.getElementById("holdings-body");
    const transactionsBody = document.getElementById("transactions-body");
    const loadingMessage = document.getElementById("loading-message");
    const errorMessage = document.getElementById("error-message");
    const holdingsTable = document.getElementById("holdings-table");
    const transactionsTable = document.getElementById("transactions-table");

    holdingsTable.style.display = "none";

    backButton.addEventListener("click", () => {
        window.location.href = "index.html";
    });

    const user = JSON.parse(localStorage.getItem("currentUser"));

    if (!user || !user.id) {
        showError("Please log in to view your holdings");
        return;
    }

    fetchHoldings(user.id);

    async function fetchHoldings(userId) {
        try {
            loadingMessage.textContent = "Loading your holdings...";
            errorMessage.classList.add("hidden");

            const response = await fetch(`/api/holdings/${userId}`, {
                headers: {
                    "Authorization": `Bearer ${user.token}`
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch holdings: ${response.status}`);
            }

            const holdings = await response.json();

            const holdingsWithNames = await Promise.all(
                holdings.map(async holding => {
                    const cryptoName = await getCryptoName(holding.crypto_id);
                    return {
                        ...holding,
                        crypto_name: cryptoName || `Crypto ID: ${holding.crypto_id}`
                    };
                })
            );

            displayHoldings(holdingsWithNames);

        } catch (error) {
            showError(error.message);
            console.error("Error fetching holdings:", error);
        }
    }

    async function getCryptoName(cryptoId) {
        try {
            const response = await fetch(`/api/name?id=${cryptoId}`, {
                headers: {
                    "Authorization": `Bearer ${user.token}`
                }
            });

            if (!response.ok) return null;
            return await response.text();
        } catch (error) {
            console.error(`Error fetching name for crypto ${cryptoId}:`, error);
            return null;
        }
    }

    function displayHoldings(holdings) {
        if (holdings.length === 0) {
            showError("You don't have any cryptocurrency holdings yet");
            return;
        }

        errorMessage.classList.add("hidden");

        holdingsBody.innerHTML = holdings.map(holding => `
            <tr>
                <td>${holding.crypto_name}</td>
                <td>${holding.quantity}</td>
                <td>${formatDate(holding.last_updated)}</td>
            </tr>
        `).join("");

        loadingMessage.style.display = "none";
        holdingsTable.style.display = "table";
    }

    fetchTransactions(user.id);

    async function fetchTransactions(userId) {
        try {
            loadingMessage.textContent = "Loading your transactions...";
            errorMessage.classList.add("hidden");

            const response = await fetch(`/api/transactions/user/${userId}`, {
                headers: {
                    "Authorization": `Bearer ${user.token}`
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch transactions: ${response.status}`);
            }

            const transactions = await response.json();

            const transactionsWithNames = await Promise.all(
                transactions.map(async transaction => {
                    const cryptoName = await getCryptoName(transaction.crypto_id);
                    return {
                        ...transaction,
                        crypto_name: cryptoName || `Crypto ID: ${holding.crypto_id}`
                    };
                })
            );

            displayTransactions(transactionsWithNames);

        } catch (error) {
            showError(error.message);
            console.error("Error fetching transactions:", error);
        }
    }

    function displayTransactions(transactions) {
        if (transactions.length === 0) {
            showError("You don't have any transactions yet");
            return;
        }

        errorMessage.classList.add("hidden");

        transactionsBody.innerHTML = transactions.map(transaction => `
            <tr>
                <td>${transaction.type}</td>
                <td>${transaction.crypto_name}</td>
                <td>${transaction.quantity}</td>
                <td>${transaction.unit_price}</td>
                <td>${transaction.total_cost}</td>
                <td>${formatDate(transaction.created_at)}</td>
            </tr>
        `).join("");

        loadingMessage.style.display = "none";
        transactionsTable.style.display = "table";
    }

    function showError(message) {
        errorMessage.textContent = message;
        errorMessage.classList.remove("hidden");
        loadingMessage.style.display = "none";
    }

    function formatDate(dateString) {
        if (!dateString) return "N/A";

        try {
            const date = new Date(dateString);
            return date.toLocaleString();
        } catch (e) {
            return dateString;
        }
    }
});