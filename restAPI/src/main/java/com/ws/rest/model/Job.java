package com.ws.rest.model;

import com.ws.rest.entity.JobInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * <p>
 * 类名称：Job
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-10-24 11:54
 * </p>
 * <p>
 * 修改人：
 * </p>
 * <p>
 * 修改时间：
 * </p>
 * <p>
 * 修改备注：
 * </p>
 * <p>
 * Copyright (c) 版权所有
 * </p>
 *
 * @version 1.0.0
 */
@Path("/job")
@Produces(MediaType.APPLICATION_JSON)
public class Job {

    @GET
    @Path("/")
    public boolean get() {
        return false;
    }

    @GET
    @Path("/{json}")
    public JobInfo getJson(@PathParam("json") String json, @QueryParam("id") String id) {
        return new JobInfo(json, id);
    }

}
