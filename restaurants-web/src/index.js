// 지도 외 일반기능

// Session 검증
// $.ajax({
// 	url: "http://localhost:8090/authentication",
// 	method: "GET",
// 	dataType: "json",
// 	success: function (res) {
// 		if (res.result == "SUCCESS") {
// 			console.log("Valid User!");
// 		} else {
// 			window.location.href = "/login.html";
// 		}
// 	},
// 	error: function () {
// 		console.log("error occured");
// 	}
// })

// 레스토랑 목록 가져오기, 실행은 index_map에서 하기로, 위치정보 로딩 완료하면 function(lat,lng) 실행

$(".restaurandList").on('click', 'tr', function (e) {

    var restaurantId = $(this).attr("restaurantId");
    console.log(restaurantId);
    console.log(e);
})

function getRestaurantList(page){
    var mapBounds = getMapBounds();
    console.log(mapBounds)
    //ajax로 가져오자

}
