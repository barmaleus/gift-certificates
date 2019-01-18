package by.rekuts.giftcertificates.view.controller;

class ControllerHelper {

    int getIdFromUrl(String pathToCreatedElement) {
        int slashIndex = pathToCreatedElement.lastIndexOf("/");
        String substr = pathToCreatedElement.substring(slashIndex+1);
        return Integer.parseInt(substr);
    }
}
