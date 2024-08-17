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

function getProductInfo(productId, farmId) {
    const name = document.getElementById("name").innerText;
    const thumbnail = document.getElementById("thumbnail").src;
    const quantity = document.getElementById("quantity").value;
    const price = parseInt(document.getElementById("price").innerText.replace(/[^\d]/g, ''), 10);
    const farmName = document.getElementById("farmName").innerText;

    return {
        farmId: farmId,
        farmName: farmName,
        products: [{
            id: productId,
            name: name,
            price: price,
            thumbnail: thumbnail,
            quantity: quantity,
        }]
    };
}

// // 상품 정보를 쿠키에 저장하는 함수
// function saveProductToCookie(data) {
//
//     const cookieName = "cart"; // 쿠키 이름
//     const existingCookie = getCookie(cookieName);
//
//     // 기존 쿠키 값이 있으면 파싱, 없으면 빈 배열 사용
//     let farms = existingCookie ? existingCookie : [];
//     // farmId가 같을 경우
//     const existingFarmIndex = farms.findIndex(farm => farm.farmId === data.farmId);
//
//     if (existingFarmIndex !== -1) {
//         // 동일한 farmId의 농장이 있으면 해당 농장의 상품 리스트를 갱신
//         let farm = farms[existingFarmIndex];
//
//         // 동일한 productId가 있는지 확인
//         const existingProductIndex = farm.products.findIndex(product => product.id === data.products.id);
//
//         if (existingProductIndex !== -1) {
//             // 동일한 productId가 있으면 해당 상품을 갱신
//             farm.products[existingProductIndex] = data.products;
//         } else {
//             // 동일한 productId가 없으면 새로운 상품 추가
//             farm.products.push(data.products);
//         }
//     } else {
//         // 기존 상품이 없으면 새로 추가
//         farms.push(data);
//     }
//
//     // 업데이트된 상품 리스트를 쿠키에 저장
//     setCookie(cookieName, farms, 7); // 7일 후 만료
// }

// 상품 정보를 쿠키에 저장하는 함수
function saveProductToCookie(data) {
    const cookieName = "cart"; // 쿠키 이름
    const existingCookie = getCookie(cookieName);

    // 기존 쿠키 값이 있으면 파싱, 없으면 빈 배열 사용
    let cart = existingCookie ? existingCookie : [];

    // 동일한 farmId의 농장이 있는지 확인
    const existingFarmIndex = cart.findIndex(item => item.farmId === data.farmId);

    if (existingFarmIndex !== -1) {
        // 동일한 farmId의 농장이 있으면 해당 농장의 상품 리스트를 갱신
        let farm = cart[existingFarmIndex];

        // 동일한 productId가 있는지 확인
        const existingProductIndex = farm.products.findIndex(product => product.id === data.products[0].id);

        if (existingProductIndex !== -1) {
            // 동일한 productId가 있으면 해당 상품을 갱신
            farm.products[existingProductIndex] = data.products[0];
        } else {
            // 동일한 productId가 없으면 새로운 상품 추가
            farm.products.push(data.products[0]);
        }

    } else {
        // 동일한 farmId가 없으면 새로운 농장 추가
        cart.push(data);
    }

    // 쿠키 만료일을 갱신하지 않고 업데이트된 상품 리스트를 쿠키에 저장
    setCookie(cookieName, cart, 7);
}

// 쿠키를 설정하는 함수 (JSON 지원)
function setCookie(cookieName, cookieValue, daysToExpire) {
    const date = new Date();
    date.setTime(date.getTime() + (daysToExpire * 24 * 60 * 60 * 1000)); // 만료일 계산
    const expires = "expires=" + date.toUTCString();
    console.log('save: ' + JSON.stringify(cookieValue));
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

// delete product
function deleteProduct(farmId, productId) {
    let cartData = getCookie('cart');

    // 3. 해당 farmId를 가진 항목 찾기
    let farm = cartData.find(farmItem => farmItem.farmId == farmId);

    if (farm) {
        // 4. 해당 farmId에서 productId를 가진 항목 삭제
        farm.products = farm.products.filter(product => product.id != productId);

        // 5. 만약 해당 farmId의 모든 product가 삭제되었다면 farmId 항목도 삭제
        if (farm.products.length === 0) {
            cartData = cartData.filter(farmItem => farmItem.farmId != farmId);
        }

        // 6. 업데이트된 cart 데이터를 다시 JSON 문자열로 변환하여 쿠키로 저장
        if (cartData.length > 0) {
            setCookie("cart", cartData, 7);
        } else {
            // 모든 항목이 삭제된 경우 쿠키를 제거
            document.cookie = "cart=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        }
    } else {
        console.log("No matching farmId found: " + farm);
    }

    alert("삭제되었습니다.");
    console.log(cartData);
    window.location.reload();
}

// 수량 수정
function alterProduct(farmId, productId, quantity) {
    let cartData = getCookie('cart');
    console.log('farmId: ' + farmId);
    console.log('productId: ' + productId);
    const farm = cartData.find(farmItem => farmItem.farmId == farmId);
    if (farm) {
        let product = farm.products.find(product => product.id == productId);

        if (product) {
            product.quantity = quantity;
            setCookie('cart', cartData, 7);
        } else {
            console.error('product not found');
        }
    } else {
        console.error('farm not found');
    }
}