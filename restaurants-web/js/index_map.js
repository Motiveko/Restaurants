// import { createPublicKey } from "crypto";

const rootUrl = "http://localhost:8090";

$(document).ready(function () {
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



	var lat;
	var lng;
	// 사용자 위치로딩
	getGeoData().then(res => {
		lat = res.lat;
		lng = res.lng;

		renderMap(lat, lng);

		// 레스토랑 정보 표시
		// renderMap후 지도 범위 가져와서 로딩
		// 이거를 
	});
})

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


// map관련 버튼이벤트 등은 랜더완료한 후에 할당해줘야할듯
function renderMap(lat, lng) {
	$("#map").css("display", "block"); // 커스텀

	var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
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
	initGeocoder()
	renderMarker(map, lat, lng);

	//원래 위치로 이동
	$(".morphBtn").off(); // 기존이벤트 제거
	$(".morphBtn").on("click", function () {
		console.log("clicked");
		morphMap(map, lat, lng);
	});

	//지도 새로고침
	$(".refreshBtn").off(); // 기존이벤트 제거
	$(".refreshBtn").on("click", function () {
		map.refresh();
		// mapBoundreset 다시할당
		// 이거는 아예 랜더링을 다시해야할듯하다..
	});

	// 기본값

	var mapBounds = map.getBounds();
	getRestaurantList(mapBounds, 1);

	function setMapBounds() {
		mapBound = map.getBounds();
	}

	function getMapBounds() {
		// 현재 맵이 보이는 위치가 바뀌어도 랜더링하거나 리셋할때만 맵바운드 바뀌고 그걸반환한다.
		return mapBounds;
	}

	function renderMarker(map, lat, lng) {
		var marker = new naver.maps.Marker({
			position: new naver.maps.LatLng(lat, lng),
			animation: naver.maps.Animation.BOUNCE, // 마커 튀기기
			map: map
		});

	}

	function morphMap(map, lat, lng) {
		var center = new naver.maps.LatLng(lat, lng);
		map.morph(center, 15, "linear");
	}

	// 지도 특정지점 클릭 시 주소,좌표 표시
	var infoWindow = new naver.maps.InfoWindow({
		anchorSkew: true
	});

	// infowindow 클릭하면 사라지게 만들고 싶은데 실패다..
	naver.maps.Event.addListener(infoWindow, "open", function (e) {
		var clickInfoWindow = naver.maps.Event.addListener(infoWindow, "click", function (e) {
			console.log("click");
			infoWindow.close();
			infoWindow.removeListener(clickInfoWindow);
		})

		var closeInfoWindow = naver.maps.Event.addListener(infoWindow, "close", function (e) {
			infoWindow.removeListener(clickInfoWindow);
			infoWindow.removeListener(closeInfoWindow);
		})

		console.log(clickInfoWindow);
	});


	function searchCoordinateToAddress(latlng) {
		console.log("searchCoordinateToAddress");

		infoWindow.close();
		naver.maps.Service.reverseGeocode({
			coords: latlng,
			orders: [
				naver.maps.Service.OrderType.ADDR,
				naver.maps.Service.OrderType.ROAD_ADDR
			].join(',')
		}, function (status, response) {
			if (status === naver.maps.Service.Status.ERROR) {
				if (!latlng) {
					return alert('ReverseGeocode Error, Please check latlng');
				}
				if (latlng.toString) {
					return alert('ReverseGeocode Error, latlng:' + latlng.toString());
				}
				if (latlng.x && latlng.y) {
					return alert('ReverseGeocode Error, x:' + latlng.x + ', y:' + latlng.y);
				}
				return alert('ReverseGeocode Error, Please check latlng');
			}

			var address = response.v2.address,
				htmlAddresses = [];

			if (address.jibunAddress !== '') {
				htmlAddresses.push('[지번 주소] ' + address.jibunAddress);
			}

			if (address.roadAddress !== '') {
				htmlAddresses.push('[도로명 주소] ' + address.roadAddress);
			}

			infoWindow.setContent([
				'<div style="padding:10px;min-width:200px;line-height:150%;">',
				'<h4 style="margin-top:5px;">선택한 주소</h4><br />',
				htmlAddresses.join('<br />'),
				'</div>'
			].join('\n'));

			infoWindow.open(map, latlng);
		});


	}

	function searchAddressToCoordinate(address) {
		naver.maps.Service.geocode({
			query: address
		}, function (status, response) {
			if (status === naver.maps.Service.Status.ERROR) {
				if (!address) {
					return alert('Geocode Error, Please check address');
				}
				return alert('Geocode Error, address:' + address);
			}

			if (response.v2.meta.totalCount === 0) {
				return alert('No result.');
			}

			var htmlAddresses = [],
				item = response.v2.addresses[0],
				point = new naver.maps.Point(item.x, item.y);

			if (item.roadAddress) {
				htmlAddresses.push('[도로명 주소] ' + item.roadAddress);
			}

			if (item.jibunAddress) {
				htmlAddresses.push('[지번 주소] ' + item.jibunAddress);
			}

			if (item.englishAddress) {
				htmlAddresses.push('[영문명 주소] ' + item.englishAddress);
			}

			infoWindow.setContent([
				'<div style="padding:10px;min-width:200px;line-height:150%;">',
				'<h4 style="margin-top:5px;">검색 주소 : ' + address + '</h4><br />',
				htmlAddresses.join('<br />'),
				'</div>'
			].join('\n'));

			map.setCenter(point);
			infoWindow.open(map, point);
		});
	}

	function initGeocoder() {
		if (!map.isStyleMapReady) {
			return;
		}
		map.addListener('click', function (e) {
			// console.log(e.coord._lat+", "+e.coord._lng );
			searchCoordinateToAddress(e.coord);
		})
		$('#address').on('keydown', function (e) {
			var keyCode = e.which;

			if (keyCode === 13) { // Enter Key
				searchAddressToCoordinate($('#address').val());
			}
		});

		$('#submit').on('click', function (e) {
			e.preventDefault();

			searchAddressToCoordinate($('#address').val());
		});
	}


	naver.maps.onJSContentLoaded = initGeocoder;
	naver.maps.Event.once(map, 'init_stylemap', initGeocoder);
}


// 일반기능들..

$(".restaurandList").on('click', 'tr', function (e) {

	var restaurantId = $(this).attr("restaurantId");
	console.log(restaurantId);
	console.log(e);
})

function getRestaurantList(mapBounds, page) {

	var data = {
		"maxLat": String(mapBounds._max._lat),
		"minLat": String(mapBounds._min._lat),
		"maxLng": String(mapBounds._max._lng),
		"minLng": String(mapBounds._min._lng),
		"page": String(page)
	};
	// var data = {
	// 	maxLat: mapBounds._max._lat,
	// 	minLat: mapBounds._min._lat,
	// 	maxLng: mapBounds._max._lng,
	// 	minLng: mapBounds._min._lng,
	// 	page: page
	// };
	// var jsonData = `{"maxLat":"37.5343707","minLat":"37.5071406","maxLng":"126.9110697","minLng":"126.8896121","page":"1"}`;
	var jsonData = JSON.stringify(data);

	console.log(data);
	$.ajax({
		url: rootUrl+"/getRestaurantList",
		contentType: 'application/json',
		dataType: "json",
		data: data,
		success: function(res){
			console.log(res);
		},
		error: function(){
			alert("실패");
		}

	})

	

}
