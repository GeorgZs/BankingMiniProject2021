# Crushers server
A basic server setup with 2 working endpoints. Confirm for yourself ;)

1. Start the server
2. Go to http://localhost:8080/ducks (in your browser or postman)
3. You will see the default ducks get generated and send back
4. Refresh and you will see they are only generated (once) when the collection is empty
5. Go to http://localhost:8080/10
6. You will see an error message since the ids generated start at 1001
7. Go to http://localhost:8080/1001
8. You will see the first duck since it got this id
