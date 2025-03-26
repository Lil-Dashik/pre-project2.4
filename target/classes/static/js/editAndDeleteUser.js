$(document).ready(function () {

    $(document).on('click', '.editUserBtn', function () {
        const userId = $(this).data('id');
        const firstName = $(this).data('firstname');
        const lastName = $(this).data('lastname');
        const age = $(this).data('age');
        const email = $(this).data('email');
        let roleIds = $(this).data('roleids');

        if (typeof roleIds === 'string') {
            roleIds = roleIds.split(',').map(r => parseInt(r.trim()));
        } else if (typeof roleIds === 'number') {
            roleIds = [roleIds];
        } else {
            roleIds = [];
        }

        console.log("Role IDs:", roleIds);

        $('#editUserId').val(userId);
        $('#editFirstName').val(firstName);
        $('#editLastName').val(lastName);
        $('#editAge').val(age);
        $('#editEmail').val(email);
        $('#editPassword').val("");


        $.ajax({
            url: "/api/admin/roles",
            method: "GET",
            success: function (roles) {
                const $roleSelect = $('#editRoles');
                $roleSelect.empty();


                roles.forEach(role => {
                    const isSelected = roleIds.includes(role.id);
                    const option = `<option value="${role.id}" ${isSelected ? 'selected' : ''}>${role.role}</option>`;
                    $roleSelect.append(option);
                });
            },
            error: function () {
                alert("Ошибка загрузки ролей для редактирования.");
            }
        });

        $('#editUserModal').modal('show');
    });

    $(document).on('submit', '#editUserForm', function (e) {
        e.preventDefault();

        const userId = $('#editUserId').val();
        const selectedRoles = $('#editRoles').val().map(Number);


        const userData = {
            id: userId,
            firstName: $('#editFirstName').val(),
            lastName: $('#editLastName').val(),
            age: parseInt($('#editAge').val()),
            email: $('#editEmail').val(),
            password: $('#editPassword').val(),
            roleIds: selectedRoles
        };

        console.log("Sending user data:", JSON.stringify(userData));


        $.ajax({
            url: `/api/admin/users/${userId}/roles`,
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(userData),
            success: function () {
                $('#editUserModal').modal('hide');
                window.refreshUsers();
            },
            error: function () {
                alert("Ошибка при редактировании пользователя.");
            }
        });
    });

    $(document).on('click', '.deleteUserBtn', function () {
        const userId = $(this).data('id');
        const firstName = $(this).data('firstname');
        const lastName = $(this).data('lastname');
        const age = $(this).data('age');
        const email = $(this).data('email');
        let roleIds = $(this).data('roleids');
        if (typeof roleIds === 'string') {
            roleIds = roleIds.split(',').map(r => parseInt(r.trim()));
        } else if (typeof roleIds === 'number') {
            roleIds = [roleIds];
        } else {
            roleIds = [];
        }

        console.log("Role IDs:", roleIds);
        $.ajax({
            url: "/api/admin/roles",
            method: "GET",
            success: function (roles) {
                const roleNames = roles.filter(role => roleIds.includes(role.id))
                    .map(role => role.role)
                    .join(", ");

                console.log("Role Names:", roleNames);

        $('#deleteUserId').val(userId);
        $('#deleteFirstName').val(firstName);
        $('#deleteLastName').val(lastName);
        $('#deleteAge').val(age);
        $('#deleteEmail').val(email);
        $('#deleteRoles').val(roleNames);

        $('#deleteUserModal').modal('show');
            },
            error: function () {
                alert("Ошибка загрузки ролей.");
            }
        });
    });

    $(document).on('submit', '#deleteUserForm', function (e) {
        e.preventDefault();

        const userId = $('#deleteUserId').val();

        $.ajax({
            url: `/api/admin/users/${userId}`,
            method: "DELETE",
            success: function () {
                $('#deleteUserModal').modal('hide');
                window.refreshUsers();
            },
            error: function () {
                alert("Ошибка при удалении пользователя.");
            }
        });
    });
});
