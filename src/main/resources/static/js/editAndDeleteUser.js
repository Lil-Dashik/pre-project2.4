$(document).ready(function () {

    $(document).on('click', '.editUserBtn', function () {
        const userId = $(this).data('id');
        const firstName = $(this).data('firstname');
        const lastName = $(this).data('lastname');
        const age = $(this).data('age');
        const email = $(this).data('email');
        const roles = $(this).data('roles').split(',').map(r => r.trim());

        $('#editUserId').val(userId);
        $('#editFirstName').val(firstName);
        $('#editLastName').val(lastName);
        $('#editAge').val(age);
        $('#editEmail').val(email);
        $('#editPassword').val("");

        $.ajax({
            url: "/api/admin/roles",
            method: "GET",
            success: function (allRoles) {
                const $roleSelect = $('#editRoles');
                $roleSelect.empty();

                allRoles.forEach(role => {
                    const isSelected = roles.includes(role.role);
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
            firstName: $('#editFirstName').val(),
            lastName: $('#editLastName').val(),
            age: parseInt($('#editAge').val()),
            email: $('#editEmail').val(),
            password: $('#editPassword').val(),
            roles: selectedRoles
        };

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
        const roles = $(this).data('roles');

        $('#deleteUserId').val(userId);
        $('#deleteFirstName').val(firstName);
        $('#deleteLastName').val(lastName);
        $('#deleteAge').val(age);
        $('#deleteEmail').val(email);
        $('#deleteRoles').val(roles);

        $('#deleteUserModal').modal('show');
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
