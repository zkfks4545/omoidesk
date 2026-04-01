
    function ChoiceDay() {
    // 숨겨진 date input을 클릭한 효과를 줍니다.
    document.getElementById('datePicker').showPicker();
}

    function sendDate(date) {
    // 선택한 날짜를 파라미터로 담아 페이지를 다시 호출합니다.
    location.href = "board?date=" + date;
}
