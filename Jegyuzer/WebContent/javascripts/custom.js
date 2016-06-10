$.ajaxSetup ({
    // Disable caching of AJAX responses
    cache: false
});
$(document).ready(function(){
	$("#div_Result").hide();
	$("#Requests").click(function(){
		alert("Request click")
	});
	$("#New").click(function(){
		alert("New click")
	});

	
	
	
	
	$("#Results").click(function(){
		openTab("Results");
		$.ajax({
			url:"./ju/result",
			async:false,
			success:function(data){
				$("#result-table").dynatable({
					  dataset: {
					    records: data
					  }}
				  );
				  $("#div_Result").show();
				},
		});
				
	});
});

function openTab(page) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(page).style.display = "block";
    document.getElementById(page).className += " active";
    
}