package com.alergb.taskmanager.exeptions;

public class AvatarNotFoundException extends RuntimeException {
    public AvatarNotFoundException(Long id){
        super("Avatar with id " + id + " not found" );
    }
}
