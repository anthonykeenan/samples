--liquibase formatted sql

--changeset R3.Corda.Generated:initial_schema_for_IOUSchemaV1

    create table iou_states (
       output_index int not null,
        transaction_id nvarchar(64) not null,
        borrower nvarchar(255),
        lender nvarchar(255),
        linear_id binary(255),
        value int,
        primary key (output_index, transaction_id)
    );
