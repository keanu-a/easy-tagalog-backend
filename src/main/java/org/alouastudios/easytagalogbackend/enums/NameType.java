package org.alouastudios.easytagalogbackend.enums;

// This enum is to only be used for Phrase entities
// This is when a phrase word contains:
// - Place name
// - Person name
// Ex: "Ako si Bob", the name "Bob" would be of NameType.PERSON since Bob is not a word in the database
public enum NameType {
    PLACE,
    PERSON
}
