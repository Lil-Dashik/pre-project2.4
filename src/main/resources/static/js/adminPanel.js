$(document).ready(function () {

    function loadUsers() {
        $.ajax({
            url: "/api/admin/users",
            method: "GET",
            dataType: "json",
            success: function (users) {
                let tableBody = "";
                users.forEach(userDTO => {
                    const roleIds = userDTO.roleIds || [];

                    $.ajax({
                        url: "/api/admin/roles",
                        method: "GET",
                        success: function (roles) {
                            const roleNames = roles
                                .filter(role => roleIds.includes(role.id))
                                .map(role => role.role)
                                .join(", ");
                            tableBody += `
                    <tr>
                        <td>${userDTO.id}</td>
                        <td>${userDTO.firstName}</td>
                        <td>${userDTO.lastName}</td>
                        <td>${userDTO.age}</td>
                        <td>${userDTO.email}</td>
                        <td>${roleNames || 'No roles'}</td>  
                        <td>
                            <button class="btn btn-info btn-sm editUserBtn"
                                    data-id="${userDTO.id}"
                                    data-firstname="${userDTO.firstName}"
                                    data-lastname="${userDTO.lastName}"
                                    data-age="${userDTO.age}"
                                    data-email="${userDTO.email}"
                                    data-roleids="${roleIds.join(',')}">
                                Edit
                            </button>
                        </td>
                        <td>
                            <button class="btn btn-danger btn-sm deleteUserBtn"
                                    data-id="${userDTO.id}"
                                    data-firstname="${userDTO.firstName}"
                                    data-lastname="${userDTO.lastName}"
                                    data-age="${userDTO.age}"
                                    data-email="${userDTO.email}"
                                    data-roleids="${roleIds.join(',')}">
                                Delete
                            </button>
                        </td>
                    </tr>`;
                            $("#usersTableBody").html(tableBody);
                        },
                        error: function () {
                            alert("Ошибка загрузки ролей.");
                        }
                    });
                });
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
                    const $roleSelect = $('#roles');
                    $roleSelect.empty();

                    roles.forEach(role => {
                        const option = `<option value="${role.id}">${role.role}</option>`;
                        $roleSelect.append(option);
                    });
                },
                error: function () {
                    alert("Ошибка загрузки ролей для редактирования.");
                }
            });

            $("#addUserForm").submit(function (e) {
                e.preventDefault();

                let userDTO = {
                    firstName: $("#firstName").val(),
                    lastName: $("#lastName").val(),
                    age: parseInt($("#age").val()),
                    email: $("#email").val(),
                    password: $("#password").val(),
                    roleIds: $("#roles").val().map(Number)
                };
                console.log("Sending user data:", JSON.stringify(userDTO));

                $.ajax({
                    url: "/api/admin/addUser",
                    method: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(userDTO),
                    success: function () {
                        alert("Пользователь успешно добавлен!");
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
