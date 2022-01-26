package api.authjava.controller.advice;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resourceName){
        super(resourceName + " not found and not authorized");
    }
}
