// import { createPublicKey } from "crypto";

const apiRootURL = "http://localhost:8090";
const webRootURL = "http://localhost:3000";

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

	// 가게 등록폼 관련 이벤트 등록
	addSubmitForm();

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
function renderMarker(map, lat, lng, isBounce) {
	var marker = new naver.maps.Marker({
		position: new naver.maps.LatLng(lat, lng),
		animation: (isBounce) ? naver.maps.Animation.BOUNCE : naver.maps.Animation.DROP, // 마커 튀기기
		map: map
	});
	return marker;
}

// 지도 특정 지점 클릭시 해당지점의 주소 표시	
function addressClick(map) {
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
// 주소검색,표시
function addSearch(map) {
	// 마커, 인포윈도 선언	
	var marker;
	var infoWindow = new naver.maps.InfoWindow({
		anchorSkew: true
	});
	initGeocoder();

	function initGeocoder() {

		if (!map.isStyleMapReady) {
			return;
		}

		map.addListener('click', function (e) {
			searchCoordinateToAddress(e.coord);
		})

		//주소 검색 form 이벤트
		$('.searchInput').on('keydown', function (e) {

			if (e.keyCode == 13) {
				e.preventDefault();
				$('.searchBtn').click();
			}
		})

		$('.searchForm').on('submit', function (e) {
			e.preventDefault();
			search($('.searchInput').val());
		});
	}


	// 지도 클릭으로 infoWindow + Marker 표시
	function searchCoordinateToAddress(latlng) {
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

			renderSearchMarker(latlng);

			// 주소 데이터 form에 넘겨주기
			$("#restaurantAddress").val(address.jibunAddress);
			infoWindow.open(map, marker);
		});
	}

	// 주소검색으로 infoWindow + Marker 표시
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
			var item = items[0];
			console.log(item);

			renderSearchMarker(item);
			searchAddressToCoordinate(item.jibunAddress);
			// 주소 데이터 form에 넘겨주기
			$("#restaurantAddress").val(item.jibunAddress);

		});

	}
	// *************************
	// 위치검색 helpers
	// *************************

	// 주소를 받아 3가지 주소로 infoWindow 표시
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
			infoWindow.open(map, marker);
		});
	}

	// 마커 표시
	function renderSearchMarker(item) {
		let lat = item.y;
		let lng = item.x;
		if (!marker) {
			marker = renderMarker(map, lat, lng, true);
		} else {
			marker.setPosition(new naver.maps.LatLng(item));
		}

		// form에 데이터 넘겨주기
		$("#lat").val(lat);
		$("#lng").val(lng);
	}

	// 이거의 역할은 정확히 파악은 안된다. 
	naver.maps.onJSContentLoaded = initGeocoder;
	naver.maps.Event.once(map, 'init_stylemap', initGeocoder);
}


function addSubmitForm(){

	$("#backBtn").on("click",function(e){

		e.preventDefault();
		window.location.href = "/index.html";
	});

	$("#actionForm").on("submit",function(e){
		e.preventDefault();
		var validation = true;

		// 이름
		if($("#restaurantName").val()==""){
			$("#nameWrap").addClass(" has-error");
			$("#nameError").css("display","inline");
			validation=false;
		}else{	
			$("#nameWrap").removeClass(" has-error");
			$("#nameError").css("display","none");
		} 
		// 주소
		if($("#restaurantAddress").val()==""){
			$("#addressWrap").addClass(" has-error");
			$("#addressError").css("display","inline");
			validation=false;
		}else {
			$("#addressWrap").removeClass(" has-error");
			$("#addressError").css("display","none");
		}
		//설명
		if($("#description").val()==""){
			$("#descriptionWrap").addClass(" has-error");
			$("#descriptionError").css("display","inline");
			validation=false;
		}else {
			$("#descriptionWrap").removeClass(" has-error");
			$("#descriptionError").css("display","none");
		}
		if(validation){
			console.log("통과");
			var formArray = $("#actionForm").serializeArray();
			var data = {};
			$.each(formArray,function(i,v){
				data[v.name] = v.value;
			});
			var string = JSON.stringify(data);
			console.log(string);
			$.ajax({
				url: apiRootURL+"/createRestaurant",
				method: "POST",
				contentType: "application/json",
				data: string,
				success: function(data, textStatus, jqXHR){
					console.log(data);
					if(data.result="SUCCESS"){
						alert("레스토랑 등록에 성공했습니다.");
						// 상세페이지로 이동하고싶은데 이거 어떻게 할 지 고민해봐야한다.
						window.location.href = webRootURL+"/index.html"; 
					} else{
						alert("레스토랑 등록에 실패했습니다.");
					}
				},
				error: function(){
					alert("error occured");
				}
			})
		}


	})
}