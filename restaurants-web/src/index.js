$(document).ready(function () {
	$.ajax({
		url: "http://localhost:8090/authentication",
		method: "GET",
		dataType: "json",
		success: function (res) {
			if (res.result == "SUCCESS") {
				console.log("Valid User!");
			} else {
				window.location.href = "/login.html";
			}
		},
		error: function () {
			console.log("error occured");
		}
	})



	console.log("I'm ready to show you restaurants");

})
