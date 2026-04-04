// 방명록 데이터 불러오기 함수
function loadGuestBoard(date = "") {
    const gbUrl = date ? `/board?date=${date}` : `/board`;

    fetch(gbUrl)
        .then(response => response.json())
        .then(showGB => {
            // 상단 날짜 업데이트
            document.getElementById('calendar-trigger').innerText = showGB.selectedDate;

            // 게시글 영역 비우기
            const gbContent = document.getElementById('gbPostText');
            gbContent.innerHTML = '';

            // 받아온 리스트를 화면에 그리기
            showGB.guestBoards.forEach(gb => {
                const htmlTemplate = `
                      <div class="gb-content-row">
                       <span class="gb-text-part">
                            ${gb.guest_nick} : <span id="guestHi_${gb.gboard_pk}"> ${gb.board_content}</span>
                       </span>
                             <a href="javascript:void(0);" onclick="editMode('${gb.gboard_pk}', '${gb.board_content}','${showGB.selectedDate}')" class="gbUp">📝</a>
                             <a href="javascript:void(0);" onclick="location.href='delGB?gboard_pk=${gb.gboard_pk}'" class="gbDel">🗑️</a>
                      </div>
                      <div class="gb-created-at">
                            ${gb.created_at}
                      </div>
               `;
                gbContent.insertAdjacentHTML('beforeend', htmlTemplate);
            })

            // 달력 기능 다시 씌워주기
            initFlatpickr(showGB.selectedDate);
        })
        .catch(error => console.error("데이터 가져오기 실패:", error));
}

// 달력 세팅
function initFlatpickr(defaultDate) {
    flatpickr('#datePicker', {
        locale: "ko",
        dateFormat: "Y-m-d",
        defaultDate: defaultDate || "today",
        onChange: function (selectedDates, dateStr) {
            loadGuestBoard(dateStr);
        }
    });

    const trigger = document.getElementById('calendar-trigger');
    trigger.onclick = function () {
        document.getElementById('datePicker')._flatpickr.open();
    };
}


// ✅ 이벤트 위임 방식으로 변경! (document 전체를 감시합니다)
document.addEventListener("submit", function (e) {
    // 폼이 제출되었을 때, 그 폼의 클래스가 'gb-write-form'이 맞는지 확인합니다.
    //다른 페이지에서 무언가 제출이 일어났을 때 이 함수 사용을 막기위한 장치!
    if (e.target && e.target.classList.contains("gb-write-form")) {

        console.log("1. submit 이벤트 감지됨!", e.target);
        e.preventDefault();
        console.log("2. 방명록 폼 확인 완료! 새로고침 차단함.");

        // 주의: document.querySelector 대신 e.target(현재 폼) 안에서 찾습니다!
        //전체를 뒤지기 보다는 내가 타겟한 페이지 안에서 찾겠다는 뜻  !
        const inputHi = e.target.querySelector(".gb-write-input");
        const contentHi = inputHi.value;

        console.log("3. 입력된 글자:", contentHi);
        if (contentHi.trim() == "") {
            alert("인사말을 남겨주세요!");
            return;
        }
        console.log("4. 서버로 fetch 요청 출발합니다!");

        fetch('/board', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                content: contentHi
            })
        })
            .then(response => response.json())
            .then(hi => {
                console.log("5. 서버통신 완료!", hi);

                if (hi.result == "success") {
                    inputHi.value = "";
                    loadGuestBoard();
                } else {
                    alert("방명록 등록에 실패했습니다.");
                }
            })
            .catch(error => console.error("통신 에러:", error));
    }
});
//method: 'POST' : 편지가 아니라 택배(데이터 전송)라는 뜻입니다.
//
// headers: { 'Content-Type': ... } : 택배 상자 안에 든 물건의 종류입니다. 여기서는 전통적인 HTML Form 데이터 형식(application/x-www-form-urlencoded)으로 보낸다고 명시했습니다. 이렇게 해야 자바 백엔드에서 평소처럼 request.getParameter("content")로 쉽게 꺼낼 수 있습니다.
//
// body : 실제 택배 내용물입니다. new URLSearchParams()를 쓰면 자바스크립트가 알아서 데이터를 content=안녕 같은 형태로 예쁘게 포장해 줍니다.
//
// .then() : 서버에서 작업이 다 끝나고 대답(response)이 돌아왔을 때 실행할 행동입니다. 여기서 새로고침 대신 loadGuestBoard()를 호출해서 새 글이 뿅! 하고 나타나게 만드는 것이 비동기의 묘미입니다.