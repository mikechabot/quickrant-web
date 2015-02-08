package com.quickrant.web.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.quickrant.web.models.Rant;
import com.quickrant.web.service.RantService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RantController extends JsonRestService {

    RantService rantService = new RantService();

    @Override
    protected void registerRequestActions() {
        registerRequestAction(new PostAction(HttpMethod.POST));
        registerRequestAction(new GetAction(HttpMethod.GET));
    }

    @Override
    protected void registerMappings() {
        registerGetMapping("/get", new GetActionRants());
        registerGetMapping("/delete", new GetActionDelete());
    }

    protected class PostAction extends Action {
        public PostAction(HttpMethod methodType) { super(methodType); }
        @Override
        public JsonElement execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            Rant rant = gson.fromJson(request.getReader(), Rant.class);
            if (rantService.postRant(rant)) {
                return AjaxResponseFactory.getSuccess("Rant posted", null).getResponse();
            } else {
                return AjaxResponseFactory.getFailure("Failed to post rant", null).getResponse();
            }
        }
    }

    protected class GetAction extends Action {
        public GetAction(HttpMethod methodType) { super(methodType); }
        @Override
        public JsonElement execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            String action = request.getPathInfo();
            if (getMappings.get(action) == null) {
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
                return null;
            } else {
                return getMappings.get(action).execute(request, response);
            }
        }
    }

    protected class GetActionRants extends Action {
        public GetActionRants() { super(HttpMethod.GET); }
        @Override
        public JsonElement execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            JsonObject data = new JsonObject();
            data.add("data", gson.toJsonTree(rantService.getRants()));
            return AjaxResponseFactory.getSuccess(null, data).getResponse();
        }
    }

    protected class GetActionDelete extends Action {
        public GetActionDelete() { super(HttpMethod.GET); }
        @Override
        public JsonElement execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            JsonObject data = new JsonObject();
            rantService.dropCollection();
            if (rantService.getRants().size() == 0) {
                return AjaxResponseFactory.getSuccess("Dropped collection", null).getResponse();
            } else {
                return AjaxResponseFactory.getFailure("didn't drop collection", null).getResponse();
            }
        }
    }

}
