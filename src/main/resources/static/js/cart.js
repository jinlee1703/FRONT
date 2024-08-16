document.getElementById("buy").addEventListener('click', function () {

});


document.getElementById("put-cart").addEventListener('click', function () {
    const productId = this.getAttribute('data-product-id');
    const user = getCookie('id');
    const data = getProductInfo(productId);
    if (user !== null) {
        // post
        postRequest({userId: user, productId: productId, quantity: document.getElementById("quantity").value});
        return;
    }
    saveProductToCookie(data);
});

function postRequest(data) {
    console.log('fetch');
    fetch('/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {
        console.log("response: " + response);
    }).catch((error) => {
        console.error(error);
    });
}

function getProductInfo(productId) {
    const name = document.getElementById("name").innerText;
    const thumbnail = document.getElementById("thumbnail").src;
    const quantity = document.getElementById("quantity").value;
    const price = document.getElementById("price").innerText;

    return {
        productId: productId,
        productName: name,
        price: price,
        thumbnail: thumbnail,
        quantity: quantity
    };
}

// 상품 정보를 쿠키에 저장하는 함수
function saveProductToCookie(data) {

    const cookieName = "cart"; // 쿠키 이름
    const existingCookie = getCookie(cookieName);

    // 기존 쿠키 값이 있으면 파싱, 없으면 빈 배열 사용
    let products = existingCookie ? existingCookie : [];
    console.log("data: " + data.productId);
    console.log("products1: " + products.length);
    // productId가 같을 경우
    const existingProductIndex = products.findIndex(product => product.productId === data.productId);

    if (existingProductIndex !== -1) {
        // 기존 상품이 있으면 업데이트
        products[existingProductIndex] = data;
    } else {
        // 기존 상품이 없으면 새로 추가
        products.push(data);
    }

    // 업데이트된 상품 리스트를 쿠키에 저장
    setCookie(cookieName, products, 7); // 7일 후 만료
}

// 쿠키를 설정하는 함수 (JSON 지원)
function setCookie(cookieName, cookieValue, daysToExpire) {
    const date = new Date();
    date.setTime(date.getTime() + (daysToExpire * 24 * 60 * 60 * 1000)); // 만료일 계산
    const expires = "expires=" + date.toUTCString();

    // JSON 값을 문자열로 변환 후 저장
    document.cookie = cookieName + "=" + encodeURIComponent(JSON.stringify(cookieValue)) + "; " + expires + "; path=/";
}

//쿠키 값을 JSON으로 가져오는 함수
function getCookie(cookieName) {
    const name = cookieName + "=";
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i].trim();
        if (cookie.indexOf(name) === 0) {
            const cookieValueStr = cookie.substring(name.length, cookie.length);
            console.log("cookie: " + decodeURIComponent(cookieValueStr));
            try {
                return JSON.parse(decodeURIComponent(cookieValueStr)); // 문자열을 JSON으로 파싱
            } catch (e) {
                console.error("Error parsing JSON cookie", e);
                return null;
            }
        }
    }
    return null;  // 쿠키가 존재하지 않을 경우 null 반환
}