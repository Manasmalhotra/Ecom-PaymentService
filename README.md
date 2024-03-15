
# Ecommerce Payment Service

Welcome to the Ecommerce Payment Service! This service provides functionalities for accepting user payments through stripe payemnt  gateway, recording them and reconciling the pending payemnts from the payment gateway.


## Tech Stack

**Programming Language:** Java

**Framework:** Spring Boot

**Databases:** MySQL

**Other Technologies:** Cron, Webhooks



## Problem Statement

Initially , I had implemented a workflow such that the payment was made before creation of order object and was recorded while confirming the order.

But later on , I identified that though this architecture works well for small-scale project but thre can be following bottlenecks if w elook forward to scale this system:

1) Dependency on external gateway front end.
2) If we wait for each user to complete the payment for which the gateway has about 5 minutes of time, it would slow down our aplication.

## Solution

To tackle these design issues, I did some research and found about the concepts of webhook and cron jobs.

I changed the user journey to generate the order as teh user decides to check out, after this they would select whatever address they want to from their address book managed by user service, then they will proceed to payment.

For Payment, we will generate a payment link using our order id as the idempotecy key.
But how do we store the payment info and now if it failed or succeded?

Here comes the concept of webhook, I configured webhook urls for payemnt success and payment faiure at stripe payment gateway.

Now whenever, the payment status gets updated the payment gateway will call the api provided by us and we will come to know about the status of teh paymet and will record it for teh respective order by extracting the idempotency key (order id) passed while generating the payment link.

Now what happens if payment gateway did call our webhook but our service was down at that time?

For tackling this issue I implemented a cron job in spring which would run every hour. The cron will get the status of all the payments marked a spending in our db and will update the same. Forthe orders for which payment failed, the job will mark their status as PAYMENT_FAILED and for those it succeded it will move them to packaging state.
