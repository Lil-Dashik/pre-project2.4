$(document).ready(function () {

    function loadUsers() {
        $.ajax({
            url: "/api/admin/users",
            method: "GET",
            dataType: "json",
            success: function (users) {
                let tableBody = "";
                users.forEach(user => {
                    const roles = user.roles.map(r => r.role).join(", ");

                    tableBody += `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.age}</td>
                        <td>${user.email}</td>
                        <td>${roles}</td>
                        <td>
                            <button class="btn btn-info btn-sm editUserBtn"
                                    data-id="${user.id}"
                                    data-firstname="${user.firstName}"
                                    data-lastname="${user.lastName}"
                                    data-age="${user.age}"
                                    data-email="${user.email}"
                                    data-roles="${roles}">
                                Edit
                            </button>
                        </td>
                        <td>
                           <button class="btn btn-danger btn-sm deleteUserBtn"
                                    data-id="${user.id}"
                                    data-firstname="${user.firstName}"
                                    data-lastname="${user.lastName}"
                                    data-age="${user.age}"
                                    data-email="${user.email}"
                                    data-roles="${roles}">
                                Delete
                            </button>
                        </td>
                    </tr>`;
                });
                $("#usersTableBody").html(tableBody);
            },
            error: function () {
                alert("Ошибка загрузки пользователей.");
            }
        });
    }

    $("#newUser-tab").click(function (e) {
        e.preventDefault();

        $("#addUserContainer").load("addUser.html", function () {

            $.ajax({
                url: "/api/admin/roles",
                method: "GET",
                success: function (roles) {
                    const $rolesSelect = $("#roles");
                    let options = roles.map(role =>
                        `<option value="${role.id}">${role.role}</option>`
                    );
                    $rolesSelect.html(options.join(""));
                },
                error: function () {
                    alert("Ошибка загрузки ролей.");
                }
            });

            $("#addUserForm").submit(function (e) {
                e.preventDefault();

                let user = {
                    firstName: $("#firstName").val(),
                    lastName: $("#lastName").val(),
                    age: parseInt($("#age").val()),
                    email: $("#email").val(),
                    password: $("#password").val(),
                    roles: $("#roles").val().map(Number)
                };

                $.ajax({
                    url: "/api/admin/addUser",
                    method: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(user),
                    success: function () {
                        alert("User added successfully!");
                        $("#users-tab").click();
                        window.refreshUsers();
                    },
                    error: function () {
                        alert("Ошибка при добавлении пользователя!");
                    }
                });
            });
        });

        $(".nav-tabs .nav-link").removeClass("active");
        $(this).addClass("active");
        $("#users-tab").removeClass("active");

        $(".tab-pane").removeClass("show active");
        $("#newUser").addClass("show active");
    });

    $("#users-tab").click(function (e) {
        e.preventDefault();

        $(".nav-tabs .nav-link").removeClass("active");
        $(this).addClass("active");
        $("#newUser-tab").removeClass("active");

        $(".tab-pane").removeClass("show active");
        $("#users").addClass("show active");
    });

    $("#editAndDeleteContainer").load("editAndDeleteUser.html");

    loadUsers();

    window.refreshUsers = loadUsers;
});
