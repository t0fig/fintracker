# Fintracker
Financial tracker api written in Java/Spring boot+PostgreSQL with JWT authentication/authorization
# Overview
You can perform all CRUD operations on users and transaction (after you get authenticated and only if you have necessary rights)
as well as get your income, spending amount or transactions and optionally filter them by their time (earliest and/or latest date can be specified)
and/or category using requests parameters.
You can try api locally, or send requests to http://fintracker.duckdns.org/
sample_requests.sh contains almost all possible kinds of request as sample.
