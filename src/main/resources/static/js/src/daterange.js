let startDate = new Date();
startDate.setDate(startDate.getDate() - 7);
startDate.setSeconds(0);
startDate.setMinutes(0);
startDate.setHours(0);
$('input[id="dateRange"]').daterangepicker(
    {
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 30,
        locale: {
            format: 'MM/DD/YYYY H:mm'
        },
        startDate: startDate,
        endDate: Date.now()
    },
    function (start, end, label) {
        let format = "MM.DD.YYYY H:mm";
        let startDate = new Date();
        let endDate = new Date();
        startDate.setTime(Date.parse(start.format(format)));
        endDate.setTime(Date.parse(end.format(format)));
        loadChart(startDate, endDate)
    });