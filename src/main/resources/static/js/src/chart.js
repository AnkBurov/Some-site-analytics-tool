window.onload = loadChart();

function loadChart(startDate, endDate) {
    if (startDate === undefined) {
        startDate = new Date();
        startDate.setDate(startDate.getDate() - 7);
        startDate.setSeconds(0);
        startDate.setMinutes(0);
        startDate.setHours(0);
    }
    if (endDate === undefined) {
        endDate = new Date();
    }

    fetch("/api?startDate=" + startDate.getTime() + "&endDate=" + endDate.getTime())
        .then(status)
        .then(json)
        .then(function (data) {
            new CanvasJS.Chart("chartContainer", {
                animationEnabled: true,
                zoomEnabled: true,
                title: {
                    text: "Observer"
                },
                toolTip:{
                    shared: true
                },
                data: [{
                    name: "posts per interval",
                    showInLegend: true,
                    type: "line",
                    dataPoints: data.first
                },
                    {
                        name: "unique posters",
                        showInLegend: true,
                        type: "line",
                        dataPoints: data.second
                    }]
            }).render();
        })
        .catch(function (err) {
            console.log('Fetch Error :-S', err);
        });
}

function status(response) {
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
}

function json(response) {
    return response.json()
}