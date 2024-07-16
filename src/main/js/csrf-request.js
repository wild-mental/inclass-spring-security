async function sendPostRequest(data) {
    try {
        // 1) Fetch the CSRF token (발급받기)
        const csrfResponse = await fetch('/csrf-token');
        if (!csrfResponse.ok) {
            throw new Error('Failed to fetch CSRF token');
        }
        const csrfData = await csrfResponse.json();
        const csrfToken = csrfData.token;
        console.log('[CSRF TOKEN ISSUED]' + csrfToken);

        // 2) Send the POST request with the CSRF token in the headers (POST 요청 테스트하기)
        const response = await fetch('/submit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken,
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            console.error(response.json())
            throw new Error('CSRF token validation failed');
        }

        // Return the response data
        return response.json();
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

// Example usage
const data = {
    name: "John Doe",
    email: "john.doe@example.com"
};

sendPostRequest(data)
    .then(responseData => {
        console.log('Response:', responseData);
    })
    .catch(error => {
        console.error('Error:', error);
    });
