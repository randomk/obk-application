# banking-application
Simple banking application to Open Bank account and process payments 
for more information read [javadocs](https://randomk.github.io/obk-application/)

### Frameworks used
- Jersey for API and HK2 CDI

### Approach
- every account created returns a reference ID that can be used in GET API
- every payment created returns a reference ID that can be used in GET API
- when payment is initiated it is set with status PENDING and put on a ArrayBlockingQueue
- A separate thread picks up the payments from the Queue, validates them and then changes the status
- the status of the payment can again be accessed by the API

### Tests
- Junits are in package: com.banking
- Integration test are in package: integration.com.banking

### End Points

#### 1. create account

##### Request:
```text

POST http://localhost:8090/accounts/

```
##### Response:

```text
"efd33ceb-e55d-445c-a2de-7b4ba3406eb8"
```

#### 2. get account balance

##### Request:
```text

GET http://localhost:8090/accounts/5bb190a5-c6b0-42c5-8baa-3bafb3fce98a/balance

```
##### Response:

```text
0
```

#### 2. make credit payment against an account

##### Request:

```text
POST http://localhost:8090/payments/credit

{
	"beneficiaryAccountId":"5bb190a5-c6b0-42c5-8baa-3bafb3fce98a",
	"amount":15
}
```
##### Response is a payment reference ID:
{
"fa2d6914-6b6c-4bf4-ae96-efa74650b947"
}


#### 3. check status of the Payment using the payment reference ID

##### Request
```text

GET http://localhost:8090/payments/fa2d6914-6b6c-4bf4-ae96-efa74650b947
```
##### Response
```text

{
    "id": "fa2d6914-6b6c-4bf4-ae96-efa74650b947",
    "beneficiaryAccountId": "5bb190a5-c6b0-42c5-8baa-3bafb3fce98a",
    "amount": 15,
    "payment": {
        "message": "Initiated - Processed successfully",
        "status": "SUCCESS"
    }
}
```

#### 4. make fund transfer payment against a payee and beneficiary account

##### Request
```text
POST http://localhost:8090/payments/transfer

{
	"payeeAccountId" :"5bb190a5-c6b0-42c5-8baa-3bafb3fce98a",
	"beneficiaryAccountId":"fa2d6914-6b6c-4bf4-ae96-efa74650b947",
	"amount":5
}
```

##### Response is again a payment reference id
```text
"ffb3117f-0a70-4f17-a06b-3267bf34ce11"
```

#### Check the status of the Fund transfer using the API again

```text
GET http://localhost:8090/payments/ffb3117f-0a70-4f17-a06b-3267bf34ce11

{
    "id": "ffb3117f-0a70-4f17-a06b-3267bf34ce11",
    "beneficiaryAccountId": "fa2d6914-6b6c-4bf4-ae96-efa74650b947",
    "amount": 5,
    "payment": {
        "message": "Initiated - Beneficiary account is Invalid",
        "status": "REJECTED"
    },
    "payeeAccountId": "5bb190a5-c6b0-42c5-8baa-3bafb3fce98a"
}

http://localhost:8090/payments/6a53c4e4-a7b8-4ad3-bd65-215c4e053a2d

{
    "id": "6a53c4e4-a7b8-4ad3-bd65-215c4e053a2d",
    "beneficiaryAccountId": "2fd95dec-6552-4d1b-a8a9-1e237fce56be",
    "amount": 5,
    "payment": {
        "message": "Initiated - Processed successfully",
        "status": "SUCCESS"
    },
    "payeeAccountId": "a99bb729-a366-417a-995d-117d0b8166dc"
}
```
