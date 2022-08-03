// read
// update
function update (boardnum) {
    if(confirm("정말 수정?")) {
        location.href = 'update?boardnum=' + boardnum;
        return;
    }

    alert("삭제를 취소하였습니다.");
}

// delete
function deleteboard (boardnum) {
    // alert(boardnum);
    if(confirm("정말 삭제?")) {
        location.href = 'delete?boardnum=' + boardnum;
        return;
    }

    alert("삭제를 취소하였습니다.");
}

// Posting
function postingChk() {
    // 제목 체크
    let title = document.getElementById('title');
    if ( title.value.length === 0 ) {
        alert("제목을 입력해주세요");
        title.focus();
        return false;
    }

    return true;
}

function pagingFormSubmit(pageNum) {
    // alert(pageNum);
    let form = document.getElementById('pagingForm');
    let page = document.getElementById('page');
    page.value = pageNum;
    form.submit();
}