
document.addEventListener("DOMContentLoaded", function () {
    const creditCardRadio = document.getElementById("creditCard");
    const bankTransferRadio = document.getElementById("bankTransfer");
    const checkOutButton = document.querySelector("#checkoutButton");
    const info = document.getElementById("info");
    const inputRequest = document.getElementById("inputRequest");
    const inputAddress = document.getElementById("inputAddress");
    const inputDetailAddress = document.getElementById("inputDetailAddress");

    function updateButtonState() {
        if (creditCardRadio.checked) {
            info.style.display = 'none';
            checkOutButton.disabled = false;
        } else if (bankTransferRadio.checked){
            info.style.display = 'block';
            checkOutButton.disabled = false;
        } else {
            info.style.display = 'none';
            checkOutButton.disabled = true;
        }
    }

        function onClickPay() {
            let orderStatus = creditCardRadio.checked ? 1 : 0;
            if (creditCardRadio.checked) {
                IMP.init("imp77114780");
                IMP.request_pay({
                    pg: "html5_inicis",
                    pay_method: "card",
                    merchant_uid: new Date().getTime(),
                    name: order.pro_name,
                    amount: totalAmount,
                    buyer_email: "iamport@siot.do",
                    buyer_name: "구매자이름",
                    buyer_tel: "010-1234-5678",
                    buyer_addr: "서울특별시 강남구 삼성동",
                    buyer_postcode: "123-456"
                }, function (rsp) {
                    if (rsp.success) {
                        const requestComment = inputRequest.value;
                        $.ajax({
                            type: "POST",
                            url: "/paymentOrders/complete",
                            data: JSON.stringify({
                                transactionId: rsp.imp_uid,
                                amount: totalAmount,
                                orderId: rsp.merchant_uid,
                                orderComment: requestComment,
                                orderAddress1: inputAddress.value,
                                orderAddress2: inputDetailAddress.value,
                                orderStatus: orderStatus
                            }),
                            contentType: "application/json",
                            success: function () {
                                window.location.href = "/paymentOrders/cart/orderComplete";
                            },
                            error: function (xhr, status, error) {
                                alert("Order completion failed: " + error);
                            }
                        });
                    } else {
                        alert("결제에 실패하였습니다.");
                    }
                });
            } else if (bankTransferRadio.checked) {
                // 은행 이체의 경우에도 AJAX 요청을 사용하여 서버로 정보 전송
                const requestComment = inputRequest.value;
                $.ajax({
                    type: "POST",
                    url: "/paymentOrders/complete",
                    data: JSON.stringify({
                        transactionId: "bank_transfer_" + new Date().getTime(), // 임시로 설정
                        amount: totalAmount, // 이체 금액
                        orderId: new Date().getTime(), // 임시로 설정
                        orderComment: requestComment,
                        orderAddress1: inputAddress.value,
                        orderAddress2: inputDetailAddress.value,
                        orderStatus: orderStatus // 은행 이체의 경우에도 전달
                    }),
                    contentType: "application/json",
                    success: function () {
                        window.location.href = "/paymentOrders/cart/orderComplete";
                    },
                    error: function (xhr, status, error) {
                        alert("Order completion failed: " + error);
                    }
                });
            }
        }

    $('#payButton').click(function () {
        IMP.request_pay({
            pg: 'html5_inicis',
            pay_method: 'card',
            merchant_uid: 'merchant_' + new Date().getTime(),
            name: 라면,
            amount: 1000,
            buyer_email: 'your_email@example.com',
            buyer_name: 'your_buyer_name',
            buyer_tel: '010-1234-5678',
            buyer_addr: 'your_buyer_address',
            buyer_postcode: '123-456'
        }, function (rsp) {
            if (rsp.success) {
                $.post('/paymentOrders/complete', function () {
                    window.location.href = '/orders';
                });
            } else {
                alert('Payment failed: ' + rsp.error_msg);
            }
        });
    });

    checkOutButton.addEventListener("click", onClickPay);
    creditCardRadio.addEventListener("change", updateButtonState);
    bankTransferRadio.addEventListener("change", updateButtonState);

    updateButtonState(); // 페이지 로드 시 초기 상태 설정
});
