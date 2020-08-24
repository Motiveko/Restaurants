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

	var centerlat;
	var centerlng;
	var map;

	// 사용자 위치로딩
	getGeoData().then(res => {
		centerlat = res.lat;
		centerlng = res.lng;

		// 맵 로딩 후 랜더링	
		renderMap(centerlat, centerlng).then(function (res) {
			map = res;
			console.log(map);
			addClicks(map, centerlat, centerlng);
			addressClick(map);
			addSearch(map);
		});
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

function addClicks(map, lat, lng) {
	// 내위치로이동
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
}

// 특정 lat,lng을 센터로 zoom:15로 맵 다시표시
function morphMap(map, lat, lng) {
	var center = new naver.maps.LatLng(lat, lng);
	map.morph(center, 15, "linear");
}

// 마커표시
function renderMarker(map, lat, lng) {
	var marker = new naver.maps.Marker({
		position: new naver.maps.LatLng(lat, lng),
		animation: naver.maps.Animation.BOUNCE, // 마커 튀기기
		map: map
	});
	return marker;
}

// 지도 특정 지점 클릭시 해당지점의 주소 표시	
function addressClick(map) {
	initGeocoder()
	//원래 위치로 이동

	// 기본값
	var mapBounds = map.getBounds();

	function setMapBounds() {
		mapBound = map.getBounds();
	}

	function getMapBounds() {
		// 현재 맵이 보이는 위치가 바뀌어도 랜더링하거나 리셋할때만 맵바운드 바뀌고 그걸반환한다.
		return mapBounds;
	}

	// 지도 특정지점 클릭 시 주소,좌표 표시
	var infoWindow = new naver.maps.InfoWindow({
		anchorSkew: true
	});

	// infowindow 클릭하면 사라지게 만들고 싶은데 실패다..
	// naver.maps.Event.addListener(infoWindow, "open", function (e) {
	// 	var clickInfoWindow = naver.maps.Event.addListener(infoWindow, "click", function (e) {
	// 		console.log("click");
	// 		infoWindow.close();
	// 		infoWindow.removeListener(clickInfoWindow);
	// 	})

	// 	var closeInfoWindow = naver.maps.Event.addListener(infoWindow, "close", function (e) {
	// 		infoWindow.removeListener(clickInfoWindow);
	// 		infoWindow.removeListener(closeInfoWindow);
	// 	})

	// 	console.log(clickInfoWindow);
	// });


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
// map관련 버튼이벤트 등은 랜더완료한 후에 할당해줘야할듯
function renderMap(lat, lng, callback) {
	return new Promise(function (resolve, reject) {

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
		map = new naver.maps.Map('map', options); //지도 생성 및 객체 리턴
		resolve(map);
	}) // end of renderMap()

}
// 주소검색
function addSearch(map){
	var marker;
	//주소 검색 form 이벤트
	$('.searchInput').on('keydown', function (e) {
		if (e.keyCode == 13) {
			$('.searchBtn').click();
		}
	})
	$('.searchForm').on('submit', function () {
		search($('.searchInput').val());
	})
	// query를 검색
	function search(query) {
		naver.maps.Service.geocode({
			query: query
		}, function (status, response) {
			if (status !== naver.maps.Service.Status.OK) {
				return alert('지도API 문제발생');
			}
	
	
			var result = response.v2, // 검색 결과의 컨테이너
				items = result.addresses, // 검색 결과의 배열
				length = items.length;
			if (length == 0) return alert("검색어를 확인해주세요");
			var item = items[length - 1];
			console.log(item);

			if(map) {
				
				let lat = item.y;
				let lng = item.x;
				if(!marker){
					marker = renderMarker(map, lat, lng);
				} else{
					marker.setPosition(new naver.maps.LatLng(lat, lng));
				}

				morphMap(map,lat,lng);
			}
			console.log(result);
		});
	}
}
