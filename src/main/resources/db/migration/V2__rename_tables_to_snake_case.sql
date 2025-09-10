BEGIN;

-- Rename tables
ALTER TABLE public."CompetitionType" RENAME TO competition_type;
ALTER TABLE public."Event" RENAME TO event;
ALTER TABLE public."Nomination" RENAME TO nomination;
ALTER TABLE public."NominationType" RENAME TO nomination_type;
ALTER TABLE public."Participant" RENAME TO participant;
ALTER TABLE public."ParticipantProfile" RENAME TO participant_profile;
ALTER TABLE public."Permission" RENAME TO permission;
ALTER TABLE public."PointTypes" RENAME TO point_types;
ALTER TABLE public."Result" RENAME TO result;
ALTER TABLE public."Role" RENAME TO role;
ALTER TABLE public."TelegramProfile" RENAME TO telegram_profile;
ALTER TABLE public."User" RENAME TO "user"; -- "user" is a reserved keyword, so it needs quotes
ALTER TABLE public."UserProfile" RENAME TO user_profile;
ALTER TABLE public."VoteLike" RENAME TO vote_like;
ALTER TABLE public."VotesTheme" RENAME TO votes_theme;
ALTER TABLE public."_PermissionToRole" RENAME TO _permission_to_role;
ALTER TABLE public."_RoleToUser" RENAME TO _role_to_user;
ALTER TABLE public.participantbase RENAME TO participant_base;

-- Rename constraints and indexes

-- Table: competition_type (was "CompetitionType")
ALTER TABLE public.competition_type RENAME CONSTRAINT "CompetitionType_pkey" TO competition_type_pkey;

-- Table: event (was "Event")
ALTER TABLE public.event RENAME CONSTRAINT "Event_pkey" TO event_pkey;
-- Note: Sequence "Contest_id_seq" might need manual check if it's tied to "Event".id

-- Table: nomination (was "Nomination")
ALTER TABLE public.nomination RENAME CONSTRAINT "Nomination_pkey" TO nomination_pkey;
ALTER TABLE public.nomination RENAME CONSTRAINT "Nomination_competitionId_fkey" TO nomination_competition_id_fkey;
ALTER TABLE public.nomination RENAME CONSTRAINT "Nomination_eventId_fkey" TO nomination_event_id_fkey;
ALTER TABLE public.nomination RENAME CONSTRAINT "Nomination_typeId_fkey" TO nomination_type_id_fkey;

-- Table: nomination_type (was "NominationType")
ALTER TABLE public.nomination_type RENAME CONSTRAINT "NominationType_pkey" TO nomination_type_pkey;

-- Table: participant (was "Participant")
ALTER TABLE public.participant RENAME CONSTRAINT "Participant_pkey" TO participant_pkey;

-- Table: participant_profile (was "ParticipantProfile")
ALTER TABLE public.participant_profile RENAME CONSTRAINT "ParticipantProfile_pkey" TO participant_profile_pkey;
ALTER TABLE public.participant_profile RENAME CONSTRAINT "ParticipantProfile_participantId_fkey" TO participant_profile_participant_id_fkey;
ALTER INDEX public."ParticipantProfile_participantId_key" RENAME TO participant_profile_participant_id_key;

-- Table: permission (was "Permission")
ALTER TABLE public.permission RENAME CONSTRAINT "Permission_pkey" TO permission_pkey;

-- Table: point_types (was "PointTypes")
ALTER TABLE public.point_types RENAME CONSTRAINT "PointTypes_pkey" TO point_types_pkey;

-- Table: result (was "Result")
ALTER TABLE public.result RENAME CONSTRAINT "Result_pkey" TO result_pkey;
ALTER TABLE public.result RENAME CONSTRAINT "Result_nominationId_fkey" TO result_nomination_id_fkey;
ALTER TABLE public.result RENAME CONSTRAINT "Result_participantId_fkey" TO result_participant_id_fkey;

-- Table: role (was "Role")
ALTER TABLE public.role RENAME CONSTRAINT "Role_pkey" TO role_pkey;

-- Table: telegram_profile (was "TelegramProfile")
ALTER TABLE public.telegram_profile RENAME CONSTRAINT "TelegramProfile_pkey" TO telegram_profile_pkey;
ALTER TABLE public.telegram_profile RENAME CONSTRAINT "TelegramProfile_userId_fkey" TO telegram_profile_user_id_fkey;
ALTER INDEX public."TelegramProfile_userId_key" RENAME TO telegram_profile_user_id_key;

-- Table: "user" (was "User")
ALTER TABLE public."user" RENAME CONSTRAINT "User_pkey" TO user_pkey;

-- Table: user_profile (was "UserProfile")
ALTER TABLE public.user_profile RENAME CONSTRAINT "UserProfile_pkey" TO user_profile_pkey;
ALTER TABLE public.user_profile RENAME CONSTRAINT "UserProfile_userId_fkey" TO user_profile_user_id_fkey;
ALTER INDEX public."UserProfile_userId_key" RENAME TO user_profile_user_id_key;

-- Table: vote_like (was "VoteLike")
ALTER TABLE public.vote_like RENAME CONSTRAINT "VoteLike_pkey" TO vote_like_pkey;
ALTER TABLE public.vote_like RENAME CONSTRAINT "VoteLike_participantId_fkey" TO vote_like_participant_id_fkey;
ALTER TABLE public.vote_like RENAME CONSTRAINT "VoteLike_userId_fkey" TO vote_like_user_id_fkey;
ALTER TABLE public.vote_like RENAME CONSTRAINT "VoteLike_votesThemeId_fkey" TO vote_like_votes_theme_id_fkey;

-- Table: votes_theme (was "VotesTheme")
ALTER TABLE public.votes_theme RENAME CONSTRAINT "VotesTheme_pkey" TO votes_theme_pkey;

-- Table: _permission_to_role (was "_PermissionToRole")
ALTER TABLE public._permission_to_role RENAME CONSTRAINT "_PermissionToRole_AB_pkey" TO _permission_to_role_ab_pkey;
ALTER TABLE public._permission_to_role RENAME CONSTRAINT "_PermissionToRole_A_fkey" TO _permission_to_role_a_fkey;
ALTER TABLE public._permission_to_role RENAME CONSTRAINT "_PermissionToRole_B_fkey" TO _permission_to_role_b_fkey;
ALTER INDEX public."_PermissionToRole_B_index" RENAME TO _permission_to_role_b_index;

-- Table: _role_to_user (was "_RoleToUser")
ALTER TABLE public._role_to_user RENAME CONSTRAINT "_RoleToUser_AB_pkey" TO _role_to_user_ab_pkey;
ALTER TABLE public._role_to_user RENAME CONSTRAINT "_RoleToUser_A_fkey" TO _role_to_user_a_fkey;
ALTER TABLE public._role_to_user RENAME CONSTRAINT "_RoleToUser_B_fkey" TO _role_to_user_b_fkey;
ALTER INDEX public."_RoleToUser_B_index" RENAME TO _role_to_user_b_index;

-- Table: participant_base (was participantbase)
ALTER TABLE public.participant_base RENAME CONSTRAINT participantbase_pkey TO participant_base_pkey;

-- Note: _prisma_migrations table is typically not renamed.

COMMIT;
