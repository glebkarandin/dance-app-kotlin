-- Этот файл будет содержать SQL-скрипт для переименования таблицы user и ее столбцов,
-- а также для переименования столбцов в других таблицах в snake_case.
-- Дополнительные проверки на другие идентификаторы с заглавными буквами также будут здесь.

BEGIN;

-- Шаг 2.1: Переименовать таблицу "user" в users
ALTER TABLE public."user" RENAME TO users;

-- Обновить ссылки на таблицу "user" в _role_to_user
-- Сначала удаляем старый constraint, потом добавляем новый
-- Примечание: Имена внешних ключей в предоставленной схеме уже в snake_case.
-- Однако, если бы они были public."_RoleToUser_B_fkey", их бы тоже пришлось переименовывать.
-- Здесь мы предполагаем, что имя constraint _role_to_user_b_fkey уже корректно (как в схеме).
ALTER TABLE public._role_to_user DROP CONSTRAINT IF EXISTS _role_to_user_b_fkey;
ALTER TABLE public._role_to_user
    ADD CONSTRAINT _role_to_user_b_fkey FOREIGN KEY ("B")
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE;

-- Обновить ссылки на таблицу "user" в telegram_profile
ALTER TABLE public.telegram_profile DROP CONSTRAINT IF EXISTS telegram_profile_user_id_fkey;
ALTER TABLE public.telegram_profile
    ADD CONSTRAINT telegram_profile_user_id_fkey FOREIGN KEY ("userId") -- столбец "userId" будет переименован позже
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT;

-- Обновить ссылки на таблицу "user" в user_profile
ALTER TABLE public.user_profile DROP CONSTRAINT IF EXISTS user_profile_user_id_fkey;
ALTER TABLE public.user_profile
    ADD CONSTRAINT user_profile_user_id_fkey FOREIGN KEY ("userId") -- столбец "userId" будет переименован позже
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT;

-- Обновить ссылки на таблицу "user" в vote_like
ALTER TABLE public.vote_like DROP CONSTRAINT IF EXISTS vote_like_user_id_fkey;
ALTER TABLE public.vote_like
    ADD CONSTRAINT vote_like_user_id_fkey FOREIGN KEY ("userId") -- столбец "userId" будет переименован позже
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT;

-- Шаг 2.2: Переименовать столбцы из camelCase в snake_case

-- Таблица: event
ALTER TABLE public.event RENAME COLUMN "dateStart" TO date_start;
ALTER TABLE public.event RENAME COLUMN "dateEnd" TO date_end;

-- Таблица: nomination
ALTER TABLE public.nomination RENAME COLUMN "eventId" TO event_id;
ALTER TABLE public.nomination RENAME COLUMN "typeId" TO type_id;
ALTER TABLE public.nomination RENAME COLUMN "competitionId" TO competition_id;
-- Обновление внешних ключей для nomination (если они были названы с camelCase)
-- В предоставленной схеме они уже snake_case: nomination_competition_id_fkey, nomination_event_id_fkey, nomination_type_id_fkey.
-- Если бы они были, например, "nomination_eventId_fkey", то их нужно было бы переименовать:
-- ALTER TABLE public.nomination RENAME CONSTRAINT "nomination_eventId_fkey" TO nomination_event_id_fkey;
-- Но так как они уже корректны, дополнительных действий не требуется.

-- Таблица: participant
ALTER TABLE public.participant RENAME COLUMN "firstName" TO first_name;
ALTER TABLE public.participant RENAME COLUMN "lastName" TO last_name;

-- Таблица: participant_base
ALTER TABLE public.participant_base RENAME COLUMN "firstName" TO first_name;
ALTER TABLE public.participant_base RENAME COLUMN "lastName" TO last_name;

-- Таблица: participant_profile
ALTER TABLE public.participant_profile RENAME COLUMN "participantId" TO participant_id;
-- Обновление внешнего ключа для participant_profile
-- В предоставленной схеме он уже snake_case: participant_profile_participant_id_fkey
-- Если бы он был "participantProfile_participantId_fkey", команда была бы:
-- ALTER TABLE public.participant_profile RENAME CONSTRAINT "participantProfile_participantId_fkey" TO participant_profile_participant_id_fkey;
-- Также индекс:
-- ALTER INDEX public."participantProfile_participantId_key" RENAME TO participant_profile_participant_id_key;
-- Но они уже корректны.

-- Таблица: result
ALTER TABLE public.result RENAME COLUMN "nominationId" TO nomination_id;
ALTER TABLE public.result RENAME COLUMN "participantId" TO participant_id;
-- Внешние ключи уже snake_case.

-- Таблица: telegram_profile
ALTER TABLE public.telegram_profile RENAME COLUMN "firstName" TO first_name;
ALTER TABLE public.telegram_profile RENAME COLUMN "lastName" TO last_name;
ALTER TABLE public.telegram_profile RENAME COLUMN "userName" TO user_name;
ALTER TABLE public.telegram_profile RENAME COLUMN "userId" TO user_id; -- Переименовываем после обновления FK constraint
ALTER TABLE public.telegram_profile RENAME COLUMN "telegramId" TO telegram_id;
-- Внешний ключ telegram_profile_user_id_fkey уже обновлен выше, чтобы ссылаться на users(id).
-- Теперь нужно убедиться, что он ссылается на переименованный столбец user_id в telegram_profile.
-- Это делается автоматически, так как FOREIGN KEY constraint ссылается на столбец по его текущему имени.

-- Таблица: user_profile
ALTER TABLE public.user_profile RENAME COLUMN "firstName" TO first_name;
ALTER TABLE public.user_profile RENAME COLUMN "lastName" TO last_name;
ALTER TABLE public.user_profile RENAME COLUMN "userId" TO user_id; -- Переименовываем после обновления FK constraint
-- Внешний ключ user_profile_user_id_fkey уже обновлен.

-- Таблица: vote_like
ALTER TABLE public.vote_like RENAME COLUMN "userId" TO user_id; -- Переименовываем после обновления FK constraint
ALTER TABLE public.vote_like RENAME COLUMN "participantId" TO participant_id;
ALTER TABLE public.vote_like RENAME COLUMN "votesThemeId" TO votes_theme_id;
ALTER TABLE public.vote_like RENAME COLUMN "createAt" TO created_at;
ALTER TABLE public.vote_like RENAME COLUMN "periodMark" TO period_mark;
-- Внешние ключи уже snake_case.

-- Таблица: votes_theme
ALTER TABLE public.votes_theme RENAME COLUMN "themeCode" TO theme_code;
ALTER TABLE public.votes_theme RENAME COLUMN "periodType" TO period_type;

-- Шаг 3: Проверить другие идентификаторы (включая последовательности)

-- Переименование последовательностей, если они содержат заглавные буквы или camelCase.
-- Из схемы видно, что некоторые последовательности имеют заглавные буквы.
-- Пример: "CompetitionType_id_seq"
-- Важно: Имена последовательностей могут быть связаны с SERIAL или IDENTITY столбцами.
-- Изменение имени последовательности может потребовать обновления определения столбца,
-- но ALTER SEQUENCE RENAME TO обычно достаточно для PostgreSQL.

ALTER SEQUENCE IF EXISTS public."CompetitionType_id_seq" RENAME TO competition_type_id_seq;
ALTER SEQUENCE IF EXISTS public."Contest_id_seq" RENAME TO event_id_seq; -- Связана с event.id
ALTER SEQUENCE IF EXISTS public."Nomination_id_seq" RENAME TO nomination_id_seq;
ALTER SEQUENCE IF EXISTS public."NominationType_id_seq" RENAME TO nomination_type_id_seq;
ALTER SEQUENCE IF EXISTS public."Participant_id_seq" RENAME TO participant_id_seq;
ALTER SEQUENCE IF EXISTS public.participantbase_id_seq RENAME TO participant_base_id_seq; -- Уже snake_case, но проверим
ALTER SEQUENCE IF EXISTS public."ParticipantProfile_id_seq" RENAME TO participant_profile_id_seq;
ALTER SEQUENCE IF EXISTS public."Permission_id_seq" RENAME TO permission_id_seq;
ALTER SEQUENCE IF EXISTS public."PointTypes_id_seq" RENAME TO point_types_id_seq;
ALTER SEQUENCE IF EXISTS public."Result_id_seq" RENAME TO result_id_seq;
ALTER SEQUENCE IF EXISTS public."Role_id_seq" RENAME TO role_id_seq;
ALTER SEQUENCE IF EXISTS public."TelegramProfile_id_seq" RENAME TO telegram_profile_id_seq;
ALTER SEQUENCE IF EXISTS public."User_id_seq" RENAME TO users_id_seq; -- Связана с users.id (ранее "user".id)
ALTER SEQUENCE IF EXISTS public."UserProfile_id_seq" RENAME TO user_profile_id_seq;
ALTER SEQUENCE IF EXISTS public."VoteLike_id_seq" RENAME TO vote_like_id_seq;
ALTER SEQUENCE IF EXISTS public."VotesTheme_id_seq" RENAME TO votes_theme_id_seq;

-- Проверка имен ограничений и индексов (многие уже были исправлены в V2 или по умолчанию snake_case)
-- Большинство ограничений и индексов в предоставленной схеме уже используют snake_case или были исправлены в V2.
-- Например, _permission_to_role_ab_pkey, _role_to_user_ab_pkey, flyway_schema_history_pk.
-- Стоит перепроверить, нет ли оставшихся.

-- Пример проверки для таблицы _prisma_migrations (обычно не изменяется, но для полноты)
-- ALTER TABLE public._prisma_migrations RENAME CONSTRAINT "_prisma_migrations_pkey" TO _prisma_migrations_pkey; -- Уже snake_case

-- Проверка столбцов "A" и "B" в связующих таблицах
-- В таблицах _permission_to_role и _role_to_user есть столбцы "A" и "B".
-- Это не camelCase, но использование одинарных букв в кавычках может быть не идеальным.
-- Однако, задание просит исправить camelCase. Оставим их как есть, если нет явного требования их менять.
-- Если бы требовалось, команды были бы:
-- ALTER TABLE public._permission_to_role RENAME COLUMN "A" TO permission_id;
-- ALTER TABLE public._permission_to_role RENAME COLUMN "B" TO role_id;
-- ALTER TABLE public._role_to_user RENAME COLUMN "A" TO role_id;
-- ALTER TABLE public._role_to_user RENAME COLUMN "B" TO user_id;
-- Это также потребовало бы обновления внешних ключей и первичного ключа.

-- Шаг 4: Переименовать столбцы "A", "B" и связанные идентификаторы в _permission_to_role и _role_to_user

-- Таблица: _permission_to_role
-- Удаляем старые внешние ключи и первичный ключ
ALTER TABLE public._permission_to_role DROP CONSTRAINT IF EXISTS _permission_to_role_ab_pkey;
ALTER TABLE public._permission_to_role DROP CONSTRAINT IF EXISTS _permission_to_role_a_fkey;
ALTER TABLE public._permission_to_role DROP CONSTRAINT IF EXISTS _permission_to_role_b_fkey;
DROP INDEX IF EXISTS public._permission_to_role_b_index;

-- Переименовываем столбцы
ALTER TABLE public._permission_to_role RENAME COLUMN "A" TO permission_id;
ALTER TABLE public._permission_to_role RENAME COLUMN "B" TO role_id;

-- Создаем новый первичный ключ
ALTER TABLE public._permission_to_role ADD CONSTRAINT _permission_to_role_pkey PRIMARY KEY (permission_id, role_id);

-- Создаем новые внешние ключи
ALTER TABLE public._permission_to_role
    ADD CONSTRAINT _permission_to_role_permission_id_fkey FOREIGN KEY (permission_id)
    REFERENCES public.permission (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE;

ALTER TABLE public._permission_to_role
    ADD CONSTRAINT _permission_to_role_role_id_fkey FOREIGN KEY (role_id)
    REFERENCES public.role (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE;

-- Создаем новый индекс
CREATE INDEX IF NOT EXISTS _permission_to_role_role_id_idx ON public._permission_to_role(role_id);


-- Таблица: _role_to_user
-- Удаляем старые внешние ключи и первичный ключ
ALTER TABLE public._role_to_user DROP CONSTRAINT IF EXISTS _role_to_user_ab_pkey;
ALTER TABLE public._role_to_user DROP CONSTRAINT IF EXISTS _role_to_user_a_fkey;
ALTER TABLE public._role_to_user DROP CONSTRAINT IF EXISTS _role_to_user_b_fkey; -- Уже был удален и пересоздан для users, но удалим снова для чистоты
DROP INDEX IF EXISTS public._role_to_user_b_index;

-- Переименовываем столбцы
ALTER TABLE public._role_to_user RENAME COLUMN "A" TO role_id;
ALTER TABLE public._role_to_user RENAME COLUMN "B" TO user_id;

-- Создаем новый первичный ключ
ALTER TABLE public._role_to_user ADD CONSTRAINT _role_to_user_pkey PRIMARY KEY (role_id, user_id);

-- Создаем новые внешние ключи
ALTER TABLE public._role_to_user
    ADD CONSTRAINT _role_to_user_role_id_fkey FOREIGN KEY (role_id)
    REFERENCES public.role (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE;

ALTER TABLE public._role_to_user
    ADD CONSTRAINT _role_to_user_user_id_fkey FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE -- Ссылается на переименованную таблицу users
    ON UPDATE CASCADE
    ON DELETE CASCADE;

-- Создаем новый индекс
CREATE INDEX IF NOT EXISTS _role_to_user_user_id_idx ON public._role_to_user(user_id);

-- Завершение транзакции
COMMIT;
