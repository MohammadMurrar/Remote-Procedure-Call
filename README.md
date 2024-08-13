# Synchronous Remote Procedure Call System
## COMP438 Project 
## Project Summary
### This project implements a Synchronous Remote Procedure Call (RPC) system capable of serving multiple clients using multiple servers. The system is designed with a central Load Distributor, which also functions as a Directory Machine, acting as an intermediary between clients and servers.

## Project Details
1. Client Process
The client process allows the user to input an integer and determines if the number is prime. It also finds the nearest prime number greater than the input. These operations are performed by remote servers via RPC. The client connects to the Directory Machine to obtain the IP and Port of an available server, then sends the RPC request to the server. The results are received and displayed to the user. The system supports multiple simultaneous clients.

2. Server Process
The server process handles two RPC functions: checking if a number is prime (isPrime(int x)) and finding the next highest prime number (nextPrime(int x)). Upon receiving a request from a client, the server processes the request and returns the results. Servers register with the Directory Machine upon startup, indicating their availability to handle client requests.

3. Dictionary Process (Load Balancer)
The Dictionary Process serves as the link between clients and servers, keeping track of available servers and distributing client requests using a Round Robin approach. It provides the IP and Port of an available server to the client without forwarding the request itself. Only one Dictionary Process is active at any time.
