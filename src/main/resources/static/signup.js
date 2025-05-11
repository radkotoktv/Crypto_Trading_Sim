document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const userData = {
            username: document.getElementById('username').value,
            password_hash: document.getElementById('password').value,
            email: document.getElementById('email').value,
            created_at: new Date().toISOString()
        };

        fetch('/api/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Account creation failed');
            }
            return response.json();
        })
        .then(data => {
            alert('Account created successfully!');
            window.location.href = 'login.html';
        })
        .catch(error => {
            alert('Error: ' + error.message);
        });
    });
});