// import { createPublicKey } from "crypto";

const rootApiUrl = "http://localhost:8090";
var markerList = [];

$(document).ready(function () {
	// 지도 외 일반기능

	// Session 검증
	// $.ajax({
	// 	url: "/authentication",
	// 	method: "GET",
	// 	dataType: "json",
	// 	success: function (res) {
	// 		console.log(res);
	// 		if (res.result == "SUCCESS") {
	// 			console.log("Valid User!");
	// 			$("#userEmail").text(res.email);
				
	// 		} else {
	// 			console.log("사용자 인증 실패..");
	// 			window.location.href = "/login";
	// 		}
	// 	},
	// 	error: function () {
	// 		console.log("error occured");
	// 		window.location.href = "/login";
	// 	}
	// })

	var lat;
	var lng;
	var map;
	// 사용자 위치로딩
	getGeoData().then(res => {
		lat = res.lat;
		lng = res.lng;
		map = renderMap(lat, lng);
		addMorphBtnEvent(map,lat,lng);
		addRefreshEvent(map);

	});
})


// ************************************************************
// ************************************************************

// 사용자 위치정보 로딩	
function getGeoData(callback) {
	return new Promise(function (resolve, reject) {

		navigator.geolocation.getCurrentPosition((pos) => {
			// callback when suceess
			var lat = pos.coords.latitude;
			var lng = pos.coords.longitude;

			resolve({
				"lat": lat,
				"lng": lng
			});
		});
	})
}


// 맵 랜더링	
function renderMap(lat, lng) {
	$("#map").css("display", "block"); // 커스텀

	// var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
	var options = { //지도를 생성할 때 필요한 기본 옵션
		center: new naver.maps.LatLng(lat, lng), //지도의 중심좌표.
		zoom: 15, //지도의 레벨(확대, 축소 정도),
		mapTypeId: naver.maps.MapTypeId.NORMAL, // 지도유형
		zoomControl: true,
		zoomControlOptions: {
			style: naver.maps.ZoomControlStyle.SMALL
		},
		scaleControl: true, // 줄자(300m,500m, ...)
		logoControl: false, // 네이버 색깔로고, 못지운다.
		mapDataControl: false, // 네이버 글자로고
		mapTypeControl: true, // 위성-일반
		zoomControl: true,
		zoomControlOptions: {
			style: naver.maps.ZoomControlStyle.LARGE,
		}
	};
	var map = new naver.maps.Map('map', options); //지도 생성 및 객체 리턴

	// 최초 map 랜더링 시 로딩하기
	var mapBounds = map.getBounds();
	getRestaurantCount(mapBounds);
	getRestaurantList(map, mapBounds, 1);
	return map;
	// renderMarker(map, lat, lng)
}

function addMorphBtnEvent(map,lat,lng){
	//원래 위치로 이동
	$(".morphBtn").off(); // 기존이벤트 제거
	$(".morphBtn").on("click", function () {
		console.log("clicked");
		morphMap(map, lat, lng);


	});
}
function addRefreshEvent(map){
	$(".refreshBtn").off(); // 기존이벤트 제거
	$(".refreshBtn").on("click", function () {
		// TODO : 맵이 현재 표시중인 곳에서 가게정보 다시로딩하게 만들기
		console.log(markerList);
		map.refresh();
		// 현재 맵에 마커 제거
		removeMarker(); 
		
		var mapBounds = map.getBounds();
		getRestaurantCount(mapBounds);
		getRestaurantList(map, mapBounds, 1);
	});
}

// 맵 기능들	


// latlng으로 맵 이동	
function morphMap(map, lat, lng) {
	var center = new naver.maps.LatLng(lat, lng);
	map.morph(center, 15, "linear");
}


// 일반기능들..

$(".restaurandList").on('click', 'tr', function (e) {
	var restaurantId = $(this).attr("restaurantId");
	// TODO : 레스토랑 상세정보 팝업	
	console.log(restaurantId);
})

function getRestaurantCount(mapBounds){
	var data = {
		"maxLat": String(mapBounds._max._lat),
		"minLat": String(mapBounds._min._lat),
		"maxLng": String(mapBounds._max._lng),
		"minLng": String(mapBounds._min._lng)
	};	
	var jsonData = JSON.stringify(data);
	console.log(jsonData);

	$.ajax({
		url: rootApiUrl + "/getRestaurantCount",
		contentType: "application/json",
		dataType: "json",
		data: data,
		success: function(res){
			console.log("getRestaurantCount :::: ")
			console.log(res);
			if( res.result=="FAILED") alert("현재 위치에 맛집이 없습니다.");
			else {
				var lastPage = res.lastPage;
				renderPagingBtn(1,0,lastPage);
				$(".lastBtn").attr("page",lastPage);

			}		
		},
		error: function (){	
			console.log("레스토랑 갯수 가져오기 실패");
		}
	})
}

// 백엔드에서 현재 맵 범위 안에 page번호의 레스토랑 목록 가져오기
function getRestaurantList(map, mapBounds, page) {

	var data = {
		"maxLat": String(mapBounds._max._lat),
		"minLat": String(mapBounds._min._lat),
		"maxLng": String(mapBounds._max._lng),
		"minLng": String(mapBounds._min._lng),
		"page": String(page)
	};
	var jsonData = JSON.stringify(data);

	$.ajax({
		url: rootApiUrl + "/getRestaurantList",
		contentType: "application/json",
		dataType: "json",
		data: data,
		success: function (res) {
			console.log(res);
			var startNum;

			if(page<=3) startNum = 1;
			else startNum = page-2;
			var lastPage = $(".lastBtn").attr("page");
			// 페이징 표시
			renderPagingBtn(startNum, page, lastPage);
			renderRestaurants(map,res);
			// 레스토랑 목록 표시
			return res;
		},
		error: function () {
			alert("실패");
		}
	})
}

// 현재 페이지를 토대로 페이지 버튼 랜더링
function renderPagingBtn(startNum, activeNum, lastPage){
	// startNum : 표시할 첫번째 번호
	// lastNum : 페이징의 마지막번호
	// activeNum : 파랑색으로 할 번호, 0이면 없다.
	
	if(activeNum == 1) $(".firstBtn").addClass(" disabled");
	else $(".firstBtn").removeClass(" disabled");
	if( activeNum == lastPage)$(".lastBtn").addClass(" disabled");
	else $(".lastBtn").removeClass(" disabled");

	var pagingBtn="";
	count = 1; // 최대 5개만 표시	
	var i;

	for( i=startNum; i<=lastPage; i++){
		if(count>5) break;
		if( i == activeNum) pagingBtn+=`<li class="${i} active" page="${i}"><span>${i}</span></li>`;
		else pagingBtn+=`<li class="${i}" page="${i}"><span>${i}</span></li>`;
		count++;
	}
	if( count>=6) pagingBtn+=`<li class="${i}" page="${i}><span>...</span></li>`;
	$(".centerPaging").html(pagingBtn);

}

function renderRestaurants(map, res){
	console.log(res);
	var list = res.list;
	var i = 0;
	var alphabet = ['A','B','C','D','E','F','G','H','I','J'];
	var content = "";
	for( i; i<list.length; i++){
		var restaurant = list[i];
		content += `<tr restaurantId=${restaurant.id}><td>${alphabet[i]}</td><td>${restaurant.name}</td>
		<td>${restaurant.address}</td><td>${restaurant.category}</td></tr>`
		renderMarker(map, restaurant.lat, restaurant.lng, i+1);
	}
	$(`.restaurants`).html(content);

}
// 맵에 마커 표시	
function renderMarker(map, lat, lng, index) {

	var icon = {
		url: `./src/images/alphabet/${index}.png`,
		scaledSize: new naver.maps.Size(30, 30),
		origin: new naver.maps.Point(0, 0),
		anchor: new naver.maps.Point(25, 26)
	};

	var marker = new naver.maps.Marker({
		map: map,
		position: new naver.maps.LatLng(lat, lng),
		animation: naver.maps.Animation.BOUNCE, // 마커 튀기기
		icon: icon
	});

	console.log(marker);

	markerList.push(marker);
	return marker;
}

// 맵에 있는 마커 제거
function removeMarker(){
	// for( var m in markerList) m.setMap(null); // 이거는 안되는데 아래는 된다. 시바..?;;
	for( var i=0; i<markerList.length; i++) markerList[i].setMap(null);
}


