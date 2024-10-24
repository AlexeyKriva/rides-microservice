package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return ride response entity"
    request {
        method 'GET'
        url '/api/rides/passengers/1'
        headers {
            header('Content-Type', 'application/vnd.fraud.v1+json')
        }
    }
    response {
        status 200
        body("""
    [
        {
                "id": 1,
                "passenger": {
                    "id": 1,
                    "name": "Ivan",
                    "email": "ivan@gmail.com",
                    "phoneNumber": "+375293578799",
                    "deleted": false
                },
                "driver": {
                    "id": 1,
                    "name": "Kirill",
                    "email": "kirill@gmail.com",
                    "phoneNumber": "+375298877123",
                    "sex": "MALE",
                    "car": {
                        "id": 1,
                        "color": "WHITE",
                        "brand": "FORD",
                        "carNumber": "1257AB-1",
                        "deleted": false
                    },
                    "deleted": false
                },
                "fromAddress": "Nezavisimosty 1",
                "toAddress": "Nezavisimosty 2",
                "rideStatus": "CREATED",
                "orderDateTime": "[2021,10,10,12,0]",
                "price": 100,
                "currency": "BYN"
        }
    ]
                """
        )
    }
}