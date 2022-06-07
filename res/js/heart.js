window.onload = function descartes() {
    let a = 40,
        start = 0,
        x = 0,
        y = 0,
        points = [];

    const acceleration = 0.1,
        max = 40;

    while (start <= max) {
        const cal = 2 * start;
        x = 50 + a * (2 * Math.cos(start) - Math.cos(cal));
        y = 50 + a * (2 * Math.sin(start) - Math.sin(cal));
        points.push([x, y]);
        start = start + acceleration;
    }

    var canvas = document.getElementById("heart_canvas");
    var context = canvas.getContext("2d");
    context.fillstyle = "#ffffff";

    var tempX = 0,
        tempY = 0;
    for (let index = 0; index < points.length; index++) {
        const element = points[index];

        const x = element[0];
        const y = element[1];
        context.moveTo(tempX, tempY);
        context.lineTo(x, y);
        context.stroke();
        tempX = x;
        tempY = y;
    }
}

function heartShanpe() {
    var x = 0,
        y = 0,
        start = 0,
        points = [];
    const max = 40,
        acceleration = 0.1;
    while (start <= max) {
        var sinres = Math.sin(start);
        x = 16 * Math.pow(sinres, 3);
        y = 13 * Math.cos(start) - 5 * Math.cos(2 * start) - 2 * Math.cos(3 * start) - Math.cos(4 * start);
        points.push([x, y]);
    }
}