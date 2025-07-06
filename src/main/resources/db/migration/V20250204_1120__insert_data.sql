INSERT INTO sys_roles (alias, name_ru, name_ky, password_length)
VALUES ('SUPER_ADMIN', 'Супер Админ', 'Супер Админ', 5),
       ('STUDENT', 'Студент', 'Студент', 5),
       ('TUTOR', 'Тутор', 'Тутор', 5);


-- Пароль: "password123" (захешированный)
INSERT INTO sys_users (name, second_name, email, phone_number, telegram, password,
                       phone_number_verified, email_verified, gender,
                       failed_login_attempts, last_login, password_change_next_logon,
                       password_last_change_time, temporary_access_until_time,
                       is_banned, deleted, edited_time, created_time)
VALUES ('Admin', 'Root', 'admin@test.com', '996700111222', '@admin',
        '$2a$10$DaMPIsKYoU/6uPnignPxN.xl8Lkmd7INBcduIYGArK/82nLotVcO6',
        TRUE, TRUE, 'MALE',
        0, CURRENT_TIMESTAMP, FALSE, CURRENT_TIMESTAMP, NULL,
        FALSE, FALSE, NULL, CURRENT_TIMESTAMP),

       ('Aizada', 'Studentova', 'student@test.com', '996700333444', '@student',
        '$2a$10$DaMPIsKYoU/6uPnignPxN.xl8Lkmd7INBcduIYGArK/82nLotVcO6',
        TRUE, TRUE, 'FEMALE',
        0, CURRENT_TIMESTAMP, FALSE, CURRENT_TIMESTAMP, NULL,
        FALSE, FALSE, NULL, CURRENT_TIMESTAMP),

       ('Nurzhan', 'Tutorov', 'tutor@test.com', '996700555666', '@tutor',
        '$2a$10$DaMPIsKYoU/6uPnignPxN.xl8Lkmd7INBcduIYGArK/82nLotVcO6',
        TRUE, TRUE, 'MALE',
        0, CURRENT_TIMESTAMP, FALSE, CURRENT_TIMESTAMP, NULL,
        FALSE, FALSE, NULL, CURRENT_TIMESTAMP);


INSERT INTO sys_user_roles (user_id, role_id)
VALUES (1, 1), -- Админ → SUPER_ADMIN
       (2, 2), -- Студент → STUDENT
       (3, 3); -- Тьютор → TUTOR


INSERT INTO hb_subjects (alias, name_ru, name_ky, deleted, edited_time)
VALUES ('math', 'Математика', 'Математика', FALSE, NULL),
       ('physics', 'Физика', 'Физика', FALSE, NULL),
       ('english', 'Английский язык', 'Англис тили', FALSE, NULL),
       ('informatics', 'Информатика', 'Информатика', FALSE, NULL),
       ('chemistry', 'Химия', 'Химия', FALSE, NULL);



INSERT INTO tutor_details (user_id, rate, price, city_id, about_me,
                            experience,
                           online, offline, at_tutor, address)
VALUES (3, -- user_id (ID тьютора)
        4.8, -- rate
        2000.0, -- price
        NULL, -- city_id (например, 1 если город известен)
        'Опытный преподаватель математики с индивидуальным подходом',
        10, -- опыт в годах
        TRUE, -- online
        TRUE, -- offline
        FALSE, -- atTutor
        'г. Бишкек, ул. Манаса, 12');

-- Предполагается, что tutor_details.id = 1
INSERT INTO tutor_education (tutor_id, university, graduation_year)
VALUES (1, 'КНУ им. Жусупа Баласагына', 2018),
       (1, 'Кыргызско-Турецкий Манас Университет', 2020);

SELECT setval(pg_get_serial_sequence('sys_roles', 'id'), (SELECT MAX(id) FROM sys_roles));
SELECT setval(pg_get_serial_sequence('sys_users', 'id'), (SELECT MAX(id) FROM sys_users));

-- По предмету Математика (id = 1)
INSERT INTO subject_experience (subject_id, tutor_id, experience_description)
VALUES (1, 1, 'Преподаю математику более 5 лет в средней школе и онлайн-платформах'),
       (1, 1, '2 года репетиторства по подготовке к олимпиадам'),
       (1, 1, '1 год работы в международной школе');

INSERT INTO error_message (id, field, message_ru, message_kg, message_eng)
VALUES ('error.not_found', 'id', 'Сущность с таким идентификатором не найдена', 'Бул ID менен объект табылган жок',
        'Entity with this ID not found'),
       ('error.validation.default', 'default', 'Ошибка валидации', 'Текшерүү катасы', 'Validation error'),
       ('error.auth', 'username', 'Неправильный логин или пароль', 'Туура эмес логин же сырсөз',
        'Invalid login or password'),
       ('error.access.timed_out', 'username', 'Время доступа истекло', 'Кирүү убактысы бүттү', 'Access timed out'),
       ('error.role.not_found', 'id', 'Роль с таким идентификатором не найдена', 'Бул идентификатор менен рол жок',
        'Role with this identifier not found'),
       ('error.user.not_found', 'id', 'Пользователь с таким идентификатором не найден',
        'Бул идентификатор менен колдонуучу табылган жок', 'User with this identifier not found'),
       ('error.alias.exists', 'alias', 'Псевдоним уже существует', 'Лакап ат мурунтан эле бар', 'Alias already exists'),
       ('error.user.not_found.role', 'role', 'Некоторые роли не найдены', 'Кээ бир ролдор табылган жок',
        'Some roles were not found'),
       ('error.valid.email.format', 'email', 'Email не соответствует формату', 'Email форматына туура келбейт',
        'Email does not match the format'),
       ('error.valid.email.not_null', 'email', 'Почта не должна быть пустой', 'Почта бош болбошу керек',
        'Email cannot be null'),
       ('error.user.not_found.email', 'email', 'Пользователь с такой почтой не найден',
        'Колдонуучу мындай почта менен табылган жок', 'The user with such email was not found'),
       ('error.valid.user.email', 'email', 'Указанная почта уже зарегистрирована',
        'Көрсөтүлгөн почта буга чейин катталган', 'The specified mail has already been registered'),

       ('error.valid.password', 'password',
        'Пароль должен соответствовать требованиям, включая заглавную букву, цифру и специальный символ и иметь длину 18',
        'Сырсөз талаптарга жооп бериши керек, анын ичинде баш тамга, сан жана атайын белги болушу жана узундугу 18 символ болушу керек',
        'The password must meet the requirements, including an uppercase letter, a number, a special character, and be 18 characters long'),

       ('error.valid.user.create.pin', 'pin',
        'ПИН-код пользователя. Формат: первые 1 или 2 цифры, затем дата рождения (ГГММДД), и последние 5 цифр случайные',
        'Колдонуучунун ПИН-коду. Формат: биринчи 1 же 2 санда, андан кийин туулган күн (ЖЖККК), жана акыркы 5 сан случайные',
        'User PIN code. Format: the first 1 or 2 digits, followed by the birth date (YYMMDD), and the last 5 digits are random'),
       ('error.valid.user.update.password', 'pin', 'Новый ПИН должен отличаться от старого ПИН',
        'Жаңы ПИН эски ПИНден айырмаланышы керек', 'New PIN must be different from the old PIN'),
       ('error.user.not_found.pin', 'pin', 'Пользователь с таким ПИН не найден',
        'Бул ПИН менен колдонуучу табылган жок', 'User with this PIN not found'),
       ('error.valid.password_change.not_own', 'pin', 'Вы не можете изменить пароль другого пользователя',
        'Сиз башка колдонуучунун сырсөзүн өзгөртө албайсыз', 'You cannot change another users password'),
       ('error.valid.old_password', 'oldPassword', 'Старый пароль не должен быть пустым',
        'Эски сырсөз бош болбошу керек', 'Old password cannot be empty'),
       ('error.valid.old_password.incorrect', 'oldPassword', 'Вы ввели неверный старый пароль',
        'Сиз эски паролду туура эмес киргиздиңиз',
        'You entered an incorrect old password'),
       ('error.user.create.pin', 'pin', 'Этот ПИН уже зарегистрирован', 'Бул ПИН мурун эле катталган',
        'This PIN is already registered'),
       ('error.access.not_activated', 'username', 'Аккаунт еще не активирован',
        'Аккаунт дагы эле активдештирилген эмес', 'Account is not yet activated'),
       ('error.activation_code.is_expires', 'activation_code',
        'Срок ссылки истек, запросите новую ссылку у администратора',
        'Шилтеменин мөөнөтү бүттү, администратордон жаңы шилтеме сураныңыз',
        'The link has expired, please request a new one from the administrator'),
       ('error.activation_code.already_activated', 'activation_code', 'Аккаунт уже активирован',
        'Аккаунт мурунтан эле активдештирилген',
        'The account is already activated'),
       ('error.user.already_activated', 'userId', 'Этот пользователь уже активирован',
        'Бул колдонуучу мурунтан эле активдештирилген',
        'This user is already activated'),
       ('error.valid.alias.not_null', 'alias', 'Уникальное наименование не должно быть пустым',
        'Уникалдуу аты бош болбошу керек', 'The unique name should not be empty'),
       ('error.valid.alias.not_unique', 'alias', 'Уникальное наименование должно быть уникальным',
        'Уникалдуу аты кайталанбашы керек', 'The unique name must be unique'),
       ('error.animal_type.not_found.id', 'id', 'Идентификатор типа животного не найден',
        'Жаныбар түрүнүн идентификатору табылган жок', 'Animal type ID not found'),
       ('error.valid.anima_type_id.not_null', 'animalTypeId', 'Укажите тип животного',
        'Жаныбарлардын түрүн көрсөтөт', 'Indicate the type of animal'),
       ('error.territorial_unit.not_found', 'id',
        'Территориальная единица не найдена', 'Аймак бирдиги табылган жок',
        'The territorial unit was not found')
        ,
       ('error.territorial_unit.code.not_found', 'code', 'Код территориальной единицы не найден',
        'Аймак бирдигинин коду табылган жок', 'The territorial unit code was not found')
        ,
       ('error.territorial_unit.region.not_found', 'region', 'Область не найдена', 'Аймак табылган жок',
        'Region not found')
        ,
       ('error.territorial_unit.area.not_found', 'area', 'Район не найден', 'Аймак табылган жок', 'Area not found'),
       ('error.plant_variety.not_found', 'plant_variety', 'Сорт растения не найден', 'Өсүмдүктүн сорту табылган жок',
        'Plant variety not found')
        ,

       ('error.crop.not_found', 'crop', 'Культура не найдена', 'Өсүмдүк табылган жок', 'Crop not found')
        ,

       ('error.region.not_found', 'region', 'Регион не найден', 'Регион табылган жок', 'Region not found')
        ,

       ('error.recommended_region.not_found', 'recommended_region', 'Рекомендуемый регион не найден',
        'Сунушталган регион табылган жок', 'Recommended region not found')
        ,

       ('error.plan.not_found', 'plan', 'План не найден', 'План табылган жок', 'Plan not found')
        ,

       ('error.head_of_organization.not_found', 'head_of_organization', 'Руководитель организации не найден',
        'Организациянын башчысы табылган жок', 'Head of organization not found')
        ,

       ('error.plan.cannot_edit_confirmed', 'plan_status', 'Подтвержденный план нельзя редактировать',
        'Ырасталган планды түзөтүү мүмкүн эмес', 'Confirmed plan cannot be edited')
        ,

       ('error.plan_status.invalid', 'plan_status', 'Некорректный статус плана', 'Пландын статусу туура эмес',
        'Invalid plan status')
        ,

       ('error.application.not_found', 'application', 'Заявка не найдена', 'Өтүнмө табылган жок',
        'Application not found')
        ,

       ('error.organization.not_found', 'organization', 'Организация не найдена', 'Организация табылган жок',
        'Organization not found')
        ,

       ('error.user.not_head_of_organization', 'user_role', 'Пользователь не является руководителем организации',
        'Колдонуучу организациянын башчысы эмес', 'User is not head of organization')
        ,
       ('error.application.status_locked', 'application_status', 'Статус заявки заблокирован',
        'Өтүнмөнүн статусу блоктолгон', 'Application status is locked')
        ,

       ('error.task_type.not_found', 'task_type', 'Тип задачи не найден', 'Тапшырманын түрү табылган жок',
        'Task type not found')
        ,

       ('error.plan_task.not_found', 'plan_task', 'Задача плана не найдена', 'Пландын тапшырмасы табылган жок',
        'Plan task not found')
        ,

       ('error.application.already.confirmed.or.rejected', 'application',
        'Заявка уже была подтверждена или отклонена',
        'Өтүнмө буга чейин жактырылган же четке кагылган',
        'Application has already been confirmed or rejected')
        ,

       ('error.comment.required.if.rejected', 'comment',
        'Комментарий обязателен при отказе заявки',
        'Өтүнмөнү четке кагууда комментарий талап кылынат',
        'Comment is required when rejecting the application')
        ,

       ('error.user.not_farmer', 'user_role',
        'Пользователь не является фермером',
        'Колдонуучу фермер эмес',
        'User is not a farmer')
        ,

       ('error.user.not_employee_or_head_of_organization', 'user_role',
        'Пользователь не является сотрудником или руководителем организации',
        'Колдонуучу организациянын кызматкери же жетекчиси эмес',
        'User is not an employee or head of organization')
        ,

       ('error.user.not_head_of_ruar', 'user_role',
        'Пользователь не является главой RUAR',
        'Колдонуучу RUAR башчысы эмес',
        'User is not head of RUAR')
        ,

       ('error.plan.cannot.be.deleted', 'plan_status',
        'Подтвержденный или активный план нельзя удалить',
        'Ырасталган же активдүү планды өчүрүү мүмкүн эмес',
        'Confirmed or active plan cannot be deleted')
        ,

       ('error.task.invalid_status', 'task_status',
        'Некорректный статус задачи',
        'Тапшырманын статусу туура эмес',
        'Invalid task status')
        ,

       ('error.task.not_allowed_for_organization', 'task',
        'Задача не принадлежит вашей организации',
        'Тапшырма сиздин организация таандык эмес',
        'Task does not belong to your organization')
        ,

       ('error.task.cannot_execute_pending', 'task_status',
        'Нельзя выполнять задачу в статусе PENDING',
        'PENDING статусундагы тапшырманы аткарууга болбойт',
        'Cannot execute task in PENDING status')
        ,

       ('error.no_tasks_for_organization', 'task',
        'Нет задач для вашей организации',
        'Сиздин организация үчүн тапшырмалар жок',
        'No tasks found for your organization')
        ,

       ('error.valid.phoneNumber.is_blank', 'phoneNumber', 'Номер телефона не должен быть пустым.',
        'Телефон номери бош болбошу керек.', 'Phone number must not be blank.')
        ,
       ('error.valid.quantity.not_null', 'quantity', 'Количество не может быть пустым', 'Саны бош болбошу керек',
        'Quantity cannot be empty')
        ,

       ('error.valid.type_productivity.not_null', 'typeProductivity', 'Тип продуктивности не может быть пустым',
        'Өндүрүмдүүлүк түрү бош болбошу керек', 'Type productivity cannot be empty')
        ,

       ('error.animal_register.not_found', 'id', 'Запись реестра животных не найдена',
        'Жаныбарлар реестри табылган жок', 'Animal register entry not found')
        ,

       ('error.bird_type.not_found.id', 'id', 'Вид птицы не найден', 'Куш түрү табылган жок', 'Bird type not found')
        ,

       ('error.fish_type.not_found.id', 'id', 'Вид рыбы не найден', 'Балык түрү табылган жок', 'Fish type not found')
        ,

       ('error.valid.animal_types.not_null', 'animalTypeIds',
        'Список идентификаторов видов животных не может быть пустым',
        'Жаныбар түрлөрүнүн идентификаторлор тизмеси бош болбошу керек',
        'List of animal type IDs cannot be empty')
        ,

       ('error.valid.transport_category.name_ru.is_blank', 'name_ru',
        'Наименование на русском языке не должно быть пустым', 'Орус тилиндеги аталышы бош болбошу керек',
        'The Russian name must not be empty')
        ,

       ('error.valid.transport_category.name_ky.is_blank', 'name_ky',
        'Наименование на кыргызском языке не должно быть пустым', 'Кыргыз тилиндеги аталышы бош болбошу керек',
        'The Kyrgyz name must not be empty')
        ,

       ('error.valid.transport_category.alias.is_blank', 'alias', 'Псевдоним не должен быть пустым',
        'Псевдоним бош болбошу керек', 'Alias must not be empty')
        ,

       ('error.valid.transport_category.alias.not_unique', 'alias', 'Псевдоним уже используется',
        'Псевдоним буга чейин колдонулган', 'Alias is already in use')
        ,
       ('error.valid.transport_categories.not_null', 'transportCategoryIds',
        'Список категорий транспорта не может быть пустым',
        'Транспорт категорияларынын тизмеси бош болбошу керек',
        'List of transport categories cannot be empty')
        ,
       ('error.valid.type_of_use.not_null', 'typeOfUse',
        'Тип использования не может быть пустым',
        'Колдонуу түрү бош болбошу керек',
        'Type of use cannot be empty')
        ,

       ('error.valid.equipment_condition.not_null', 'equipmentCondition',
        'Состояние оборудования не может быть пустым',
        'Жабдуунун абалы бош болбошу керек',
        'Equipment condition cannot be empty')
        ,

       ('error.valid.year_of_manufacture.not_null', 'yearOfManufacture',
        'Год выпуска не может быть пустым',
        'Чыгарылган жылы бош болбошу керек',
        'Year of manufacture cannot be empty')
        ,

       ('error.valid.period_start.not_null', 'periodStart',
        'Дата начала периода не может быть пустой',
        'Мөөнөттүн башталышы бош болбошу керек',
        'Period start date cannot be empty')
        ,

       ('error.valid.period_end.not_null', 'periodEnd',
        'Дата окончания периода не может быть пустой',
        'Мөөнөттүн аягы бош болбошу керек',
        'Period end date cannot be empty')
        ,

       ('error.auth.organization_not_found', 'organization',
        'У авторизованного пользователя отсутствует организация',
        'Авторизацияланган колдонуучуда организация жок',
        'The authorized user does not have a organization')
        ,

       ('error.valid.task_type_id.does_not_belong', 'taskTypeId',
        'Тип задачи не относится к организации пользователя',
        'Тапшырманын түрү колдонуучунун организация тиешелүү эмес',
        'The task type does not belong to the user`s organization')
        ,

       ('error.valid.organization_id', 'organizationId',
        'Необходимо указать организацию',
        'Организация көрсөтүү керек',
        'The organization must be specified')
        ,

       ('error.phone.invalid_format', 'phoneNumber', 'Номер телефона должен быть в формате +996xxxxxxxxx.',
        'Телефон номери +996xxxxxxxxx форматында болушу керек.', 'Phone number must be in the format +996xxxxxxxxx.')
        ,
       ('error.phone.invalid_number', 'phoneNumber',
        'Номер телефона недействителен.',
        'Телефон номери жараксыз.',
        'Invalid phone number.'),

       ('error.valid.crop_id.not_null', 'cropId', 'Необходимо выбрать культуру.', 'Өсүмдүктү тандоо керек.',
        'A crop must be selected.')
        ,

       ('error.valid.region_id.not_null', 'regionId', 'Необходимо выбрать область.', 'Областы тандоо керек.',
        'A region must be selected.')
        ,

       ('error.crop_region.not_found', 'id', 'Запись не найдена.', 'Жазуу табылган жок.', 'Record not found.'),

       ('error.valid.longitude.not_null', 'longitude', 'Долгота местоположения не может быть пустой',
        'Жердин узундугу бош болбошу керек', 'Longitude cannot be empty'),

       ('error.valid.latitude.not_null', 'latitude', 'Широта местоположения не может быть пустой',
        'Жердин эндиги бош болбошу керек', 'Latitude cannot be empty'),

       ('error.access.denied', 'username', 'Доступ закрыт', 'Кирүү жабык', 'Access denied'),

       ('error.token.expired_or_invalid', 'username', 'Срок действия токена истек или он недействителен',
        'Токендин мөөнөтү бүттү же жараксыз', 'Token expired or invalid'),

       ('error.mail.send_failed', 'email',
        'Не удалось отправить письмо на указанный адрес',
        'Көрсетүлгөн дарекке кат жөнөтүү мүмкүн болгон жок', 'Failed to send email to the specified address'),

       ('error.valid.user.phone.exists',
        'phone',
        'Пользователь с таким номером телефона уже существует',
        'Мындай телефон номери бар колдонуучу мурунтан эле бар',
        'A user with this phone number already exists'),
       ('error.service_type.not_found.id', 'serviceTypeId',
        'Тип услуги с таким ID не найден',
        'Берилген ID менен кызмат түрү табылган жок',
        'Service type with this ID not found'),

       ('error.service_category.not_found.id', 'serviceCategoryId',
        'Категория услуги с таким ID не найдена',
        'Берилген ID менен категория түрү табылган жок',
        'Service category with this ID not found'),

       ('error.service_register.not_found', 'id',
        'Запись в реестре услуг не найдена',
        'Кызмат реестринде мындай жазуу табылган жок',
        'Service register entry not found'),

       ('error.supplier.not_found', 'supplierId',
        'Поставщик услуги не найден',
        'Кызмат көрсөтүүчү табылган жок',
        'Supplier not found'),

       ('error.service_photo.not_found', 'serviceId',
        'Фото для данной услуги не найдено',
        'Бул кызмат үчүн сүрөт табылган жок',
        'Photo for this service not found'),

       ('error.password.too_short', 'password', 'Пароль должен содержать минимум %d символов',
        'Купуя сөз кеминде %d белгиден турушу керек', 'Password must be at least %d characters long'),
       ('error.password.reset_limit', 'email', 'Лимит сброса пароля исчерпан (3 раза в день)',
        'Купуя сөздү калыбына келтирүүнүн чеги аяктады (күнүнө 3 жолу)',
        'Password reset limit exceeded (3 times per day)'),
       ('error.phone.service.invalid_format',
        'phone',
        'Номер телефона {0} недействителен или не соответствует формату страны.',
        'Телефон номери {0} туура эмес же өлкөнүн форматына туура келбейт.',
        'Phone number {0} is invalid or does not match the expected country format.'),
       ('error.plan.cannot_be_confirmed_overdue', 'application',
        'План не может быть одобрен, так как он просрочен.',
        'План мөөнөтүнөн өтүп кеткендиктен бекитүүгө болбойт.',
        'The plan cannot be approved because it is overdue.'),
       ('error.plan.is_overdue', 'plan',
        'План просрочен и не может быть обработан.',
        'План мөөнөтүнөн өтүп кеткендиктен иштетилбейт.',
        'The plan is overdue and cannot be processed.'),
       ('error.valid.service_category_id.not_null', 'serviceCategoryId',
        'Категория услуги не может быть пустой', 'Кызмат түрүнүн категориясы бош болбошу керек',
        'Service category must not be null'),

       ('error.valid.area_id.not_null', 'areaId',
        'Район не может быть пустым', 'Район бош болбошу керек',
        'Area must not be null'),

       ('error.valid.service_type_id.not_null', 'serviceTypeId',
        'Тип услуги не может быть пустым', 'Кызмат түрү бош болбошу керек',
        'Service type must not be null'),
       ('error.valid.service_name.not_blank', 'serviceName',
        'Название услуги не может быть пустым', 'Кызматтын аталышы бош болбошу керек',
        'Service name must not be blank'),

       ('error.valid.price.not_null', 'price',
        'Цена не может быть пустой', 'Баасы бош болбошу керек',
        'Price must not be null'),

       ('error.valid.country_code.not_blank', 'countryCode',
        'Код страны не может быть пустым', 'Өлкөнүн коду бош болбошу керек',
        'Country code must not be blank'),

       ('error.valid.address.not_blank', 'address',
        'Адрес не может быть пустым', 'Дарек бош болбошу керек',
        'Address must not be blank'),
       ('error.create_directory', 'file', 'Не удалось создать директорию для сохранения файла',
        'Файл сактоочу каталог түзүлгөн жок', 'Failed to create directory for file storage'),

       ('error.file_not_found', 'file', 'Файл не найден', 'Файл табылган жок', 'File not found'),

       ('error.file_body.unacceptable.format', 'file',
        'Недопустимый формат файла. Разрешены только PNG, JPG, JPEG, PDF',
        'Файлдын форматы туура эмес. Болгону PNG, JPG, JPEG, PDF форматтарына уруксат берилет',
        'Unsupported file format. Only PNG, JPG, JPEG, and PDF are allowed'),

       ('error.file_save_failed', 'file', 'Не удалось сохранить файл', 'Файл сакталбай калды', 'Failed to save file'),
       ('error.product_category.not_found.id', 'productCategoryId',
        'Категория продукта с таким ID не найдена',
        'Бул ID менен продукт категориясы табылган жок',
        'Product category with this ID not found'),
       ('error.valid.product_name.not_blank', 'productName',
        'Название продукта не должно быть пустым',
        'Продукттун аты бош болбошу керек',
        'Product name must not be blank'),

       ('error.valid.delivery.not_null', 'delivery',
        'Поле "Доставка" обязательно для заполнения',
        '"Жеткирүү" талаасы сөзсүз толтурулушу керек',
        '"Delivery" field must not be null'),
       ('error.valid.amount.not_null', 'amount',
        'Поле "Количество/Объем" обязательно для заполнения',
        '"Саны/Көлөмү" талаасы сөзсүз толтурулушу керек',
        '"Amount" field must not be null'),
       ('error.product_register.not_found', 'id',
        'Продукт с таким ID не найден',
        'Бул ID менен продукт табылган жок',
        'Product with this ID not found'),
       ('error.photo_required',
        'files',
        'Необходимо загрузить хотя бы одно фото',
        'Кызмат үчүн жок дегенде бир сүрөт жүктөш керек',
        'At least one service photo is required'),
       ('error.unit_measurement.not_found.id', 'id',
        'Единица измерения с таким ID не найдена',
        'Бул ID менен бирдик табылган жок',
        'Unit measurement with this ID not found'),

       ('error.valid.unit_measurement.name_ru.not_null', 'nameRu',
        'Название на русском языке обязательно',
        'Орус тилиндеги аталыш милдеттүү',
        'Name in Russian is required'),

       ('error.valid.unit_measurement.name_ky.not_null', 'nameKy',
        'Название на кыргызском языке обязательно',
        'Кыргыз тилиндеги аталыш милдеттүү',
        'Name in Kyrgyz is required'),

       ('error.valid.unit_measurement_id.not_null', 'unitMeasurementId',
        'ID единицы измерения обязателен',
        'Өлчөө бирдигинин IDси милдеттүү',
        'Unit measurement ID is required');


INSERT INTO hb_cities (alias, name_ru, name_ky)
VALUES ('bishkek', 'Бишкек', 'Бишкек'),
       ('osh', 'Ош', 'Ош'),
       ('jalal-abad', 'Джалал-Абад', 'Жалал-Абад'),
       ('karakol', 'Каракол', 'Каракол'),
       ('naryn', 'Нарын', 'Нарын'),
       ('talas', 'Талас', 'Талас'),
       ('tokmok', 'Токмок', 'Токмок'),
       ('kant', 'Кант', 'Кант'),
       ('batken', 'Баткен', 'Баткен'),
       ('balykchy', 'Балыкчы', 'Балыкчы'),
       ('kara-balta', 'Кара-Балта', 'Кара-Балта'),
       ('kara-suu', 'Кара-Суу', 'Кара-Суу');
