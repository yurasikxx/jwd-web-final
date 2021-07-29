function time() {
    let date = new Date();
    let hours = date.getHours();
    let minutes = date.getMinutes();
    let seconds = date.getSeconds();
    document.getElementById("time").innerHTML = hours + ":" + minutes + ":" + seconds;
    setTimeout("time();", 1000);
}