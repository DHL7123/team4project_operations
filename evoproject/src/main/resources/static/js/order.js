
document.addEventListener("DOMContentLoaded", function () {
    const creditCardRadio = document.getElementById("creditCard");
    const bankTransferRadio = document.getElementById("bankTransfer");
    const checkOutButton = document.querySelector("#checkoutButton");
    const info = document.getElementById("info");

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
        if (creditCardRadio.checked) {
            IMP.init("imp77114780");
            IMP.request_pay({
                pg: "html5_inicis",
                pay_method: "card",
                merchant_uid: "merchant_" + new Date().getTime(),//merchant부터 buyer까지 연결 및 필요없는거 제거해서 쓰면 된다.
                name: "주문명:결제테스트",
                amount: 1000,
                buyer_email: "iamport@siot.do",
                buyer_name: "구매자이름",
                buyer_tel: "010-1234-5678",
                buyer_addr: "서울특별시 강남구 삼성동",
                buyer_postcode: "123-456"
            }, function (rsp) {
                if (rsp.success) {
                    alert("결제가 완료되었습니다.");
                } else {
                    alert("결제에 실패하였습니다.");
                }
            });
        } else if (bankTransferRadio.checked) {
            window.location.href = "bankTransfer.html";
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
