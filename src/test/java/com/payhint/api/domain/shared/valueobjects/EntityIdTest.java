package com.payhint.api.domain.shared.valueobjects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.payhint.api.domain.shared.exceptions.InvalidPropertyException;

public abstract class EntityIdTest<T extends EntityId> {

    protected static final UUID VALID_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    protected static final String VALID_UUID_STRING = "123e4567-e89b-12d3-a456-426614174000";

    protected abstract T createEntityId(UUID uuid);

    protected abstract T createEntityIdFromString(String id);

    protected abstract String getEntityName();

    @Nested
    @DisplayName("Valid EntityId Creation Tests")
    class ValidEntityIdCreationTests {

        @Test
        @DisplayName("Should create EntityId with valid UUID")
        void shouldCreateEntityIdWithValidUUID() {
            T entityId = createEntityId(VALID_UUID);

            assertThat(entityId).isNotNull();
            assertThat(entityId.toString()).isEqualTo(VALID_UUID.toString());
        }

        @Test
        @DisplayName("Should create EntityId from valid string")
        void shouldCreateEntityIdFromValidString() {
            T entityId = createEntityIdFromString(VALID_UUID.toString());

            assertThat(entityId).isNotNull();
            assertThat(entityId.toString()).isEqualTo(VALID_UUID.toString());
        }

        @Test
        @DisplayName("Should create EntityId from random UUID")
        void shouldCreateEntityIdFromRandomUUID() {
            UUID randomUuid = UUID.randomUUID();
            T entityId = createEntityId(randomUuid);

            assertThat(entityId.toString()).isEqualTo(randomUuid.toString());
        }

        @Test
        @DisplayName("Should create EntityId from uppercase UUID string")
        void shouldCreateEntityIdFromUppercaseUUIDString() {
            String uppercaseUuid = VALID_UUID_STRING.toUpperCase();
            T entityId = createEntityIdFromString(uppercaseUuid);

            assertThat(entityId.toString()).isEqualTo(VALID_UUID.toString());
        }

        @Test
        @DisplayName("Should create EntityId from mixed case UUID string")
        void shouldCreateEntityIdFromMixedCaseUUIDString() {
            String mixedCaseUuid = "123E4567-e89B-12D3-A456-426614174000";
            T entityId = createEntityIdFromString(mixedCaseUuid);

            assertThat(entityId.toString()).isEqualTo(VALID_UUID.toString());
        }
    }

    @Nested
    @DisplayName("Invalid EntityId Validation Tests")
    class InvalidEntityIdValidationTests {

        @Test
        @DisplayName("Should reject null UUID")
        void shouldRejectNullUUID() {
            assertThatThrownBy(() -> createEntityId(null)).isInstanceOf(InvalidPropertyException.class)
                    .hasMessageContaining("ID cannot be null");
        }

        @Test
        @DisplayName("Should reject null string in fromString")
        void shouldRejectNullStringInFromString() {
            assertThatThrownBy(() -> createEntityIdFromString(null)).isInstanceOf(InvalidPropertyException.class)
                    .hasMessageContaining("Invalid ID format");
        }

        @ParameterizedTest
        @ValueSource(strings = { "", "   ", "invalid", "123", "not-a-uuid", "123e4567-e89b-12d3-a456",
                "123e4567-e89b-12d3-a456-42661417400g", "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx" })
        @DisplayName("Should reject invalid UUID strings")
        void shouldRejectInvalidUUIDStrings(String invalidUuid) {
            assertThatThrownBy(() -> createEntityIdFromString(invalidUuid)).isInstanceOf(InvalidPropertyException.class)
                    .hasMessageContaining("Invalid ID format");
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should be immutable as a record")
        void shouldBeImmutable() {
            T entityId = createEntityId(VALID_UUID);
            UUID originalValue = entityId.value();

            assertThat(entityId.toString()).isEqualTo(originalValue.toString());
        }

        @Test
        @DisplayName("Should maintain value integrity")
        void shouldMaintainValueIntegrity() {
            T originalEntityId = createEntityId(VALID_UUID);
            T retrievedEntityId = originalEntityId;

            assertThat(retrievedEntityId).isEqualTo(originalEntityId);
            assertThat(retrievedEntityId.toString()).isEqualTo(VALID_UUID.toString());
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when UUIDs are the same")
        void shouldBeEqualWhenUUIDsAreSame() {
            T entityId1 = createEntityId(VALID_UUID);
            T entityId2 = createEntityId(VALID_UUID);

            assertThat(entityId1).isEqualTo(entityId2);
            assertThat(entityId1.hashCode()).isEqualTo(entityId2.hashCode());
        }

        @Test
        @DisplayName("Should be equal when created from same string")
        void shouldBeEqualWhenCreatedFromSameString() {
            T entityId1 = createEntityIdFromString(VALID_UUID_STRING);
            T entityId2 = createEntityIdFromString(VALID_UUID_STRING);

            assertThat(entityId1).isEqualTo(entityId2);
            assertThat(entityId1.hashCode()).isEqualTo(entityId2.hashCode());
        }

        @Test
        @DisplayName("Should be equal regardless of case in string")
        void shouldBeEqualRegardlessOfCaseInString() {
            T entityId1 = createEntityIdFromString(VALID_UUID_STRING.toLowerCase());
            T entityId2 = createEntityIdFromString(VALID_UUID_STRING.toUpperCase());

            assertThat(entityId1).isEqualTo(entityId2);
            assertThat(entityId1.hashCode()).isEqualTo(entityId2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when UUIDs are different")
        void shouldNotBeEqualWhenUUIDsAreDifferent() {
            T entityId1 = createEntityId(UUID.randomUUID());
            T entityId2 = createEntityId(UUID.randomUUID());

            assertThat(entityId1).isNotEqualTo(entityId2);
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("Should return UUID string in toString")
        void shouldReturnUUIDStringInToString() {
            T entityId = createEntityId(VALID_UUID);

            assertThat(entityId.toString()).isEqualTo(VALID_UUID_STRING.toString());
        }

        @Test
        @DisplayName("Should return lowercase UUID in toString")
        void shouldReturnLowercaseUUIDInToString() {
            T entityId = createEntityIdFromString(VALID_UUID_STRING.toUpperCase());

            assertThat(entityId.toString()).isEqualTo(VALID_UUID_STRING.toString());
        }

        @Test
        @DisplayName("Should be reversible with fromString")
        void shouldBeReversibleWithFromString() {
            T originalEntityId = createEntityId(VALID_UUID);
            String stringRepresentation = originalEntityId.toString();
            T recreatedEntityId = createEntityIdFromString(stringRepresentation);

            assertThat(recreatedEntityId.toString()).isEqualTo(originalEntityId.toString());
        }
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create same EntityId from constructor and fromString")
        void shouldCreateSameEntityIdFromConstructorAndFromString() {
            T entityIdFromConstructor = createEntityId(VALID_UUID);
            T entityIdFromString = createEntityIdFromString(VALID_UUID_STRING);

            assertThat(entityIdFromString.toString()).isEqualTo(entityIdFromConstructor.toString());
        }

        @Test
        @DisplayName("Should handle fromString with various UUID formats")
        void shouldHandleFromStringWithVariousUUIDFormats() {
            String[] validFormats = { VALID_UUID_STRING, VALID_UUID_STRING.toUpperCase(),
                    VALID_UUID_STRING.toLowerCase(), "123E4567-E89B-12D3-A456-426614174000" };

            for (String format : validFormats) {
                T entityId = createEntityIdFromString(format);
                assertThat(entityId.toString()).isEqualTo(VALID_UUID.toString());
            }
        }
    }
}
