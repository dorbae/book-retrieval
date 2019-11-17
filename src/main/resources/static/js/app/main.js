/*****************************************************************
 *
 * main.js
 *
 * @description 메인 Javascript
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/13 16:40     dorbae    최초 생성
 * @since 2019/11/13 16:40
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
var main = {
    init: function () {
        var _this = this;
        $('#btn-signin').on('click', function () {
            _this.signIn();
        });
        $('#btn-signup').on('click', function () {
            _this.signUp();
        });
        $('#btn-retrieval-book').on('click', function () {
            _this.retrievalBooks();
        });
    },
    openRecentRetrievalModal: function () {
        $('#modal-recent-retrieval').modal(show);
    },
    retrievalBooks: function () {
        var keyword = $('#keyword').val();
        var url = "/books?keyword=" + keyword;
        var ret = window.open(url);
        // $('#div-book-list').load(url);
    },
    /*
     * 로그인
     */
    signIn: function () {
        var data = {
            userId: $('#userId').val(),
            userPassword: $('#userPassword').val(),
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/sessions',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data) {
            alert('로그인 되었습니다.');
            location.reload();
        }).fail(function (error) {
            alert(error);
        });
    },
    /*
     * 회원가입
     */
    signUp: function () {
        var data = {
            userId: $('#signUpUserId').val(),
            userPassword: $('#signUpUserPassword').val(),
            userName: $('#signUpUserName').val(),
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/users',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data) {
            alert('가입 되셨습니다.');
            location.reload();
        }).fail(function (error) {
            alert(error);
        });
    }
};

main.init();