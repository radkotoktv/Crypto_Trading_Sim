document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');

    form.addEventListener('submit', async function(event) {
        event.preventDefault();

        try {
            const response = await fetch('/api/assetpairs');

            if (!response.ok) {
                throw new Error("Failed to trigger script");
            }
        } catch (error) {
            document.getElementById("status").textContent = "Error: " + error.message;
            console.error("Error:", error);
            return;
        }

        const userData = {
            username: document.getElementById('username').value,
            password_hash: document.getElementById('password').value,
            email: document.getElementById('email').value,
            created_at: new Date().toISOString()
        };

        try {
            const createResponse = await fetch('/api/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });

            if (!createResponse.ok) {
                throw new Error('Account creation failed');
            }

            const data = await createResponse.json();
            alert('Account created successfully!');
            window.location.href = 'login.html';

        } catch (error) {
            alert('Error: ' + error.message);
        }
    });
});
