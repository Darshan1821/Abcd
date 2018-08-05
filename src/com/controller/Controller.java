package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beans.BabySitter;
import com.beans.Toddler;
import com.google.gson.Gson;
import com.service.Service;

@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Controller() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String source = request.getParameter("action");
		Service service = new Service();

		if (source.equalsIgnoreCase("getBabySitters")) {
			try {
				ArrayList<BabySitter> babySitters = service.getBabySitterNames();
				String babySitterJson = new Gson().toJson(babySitters);
				response.setContentType("application/json");
				response.getWriter().write(babySitterJson);
			} catch (SQLException e) {
				response.setStatus(503);
				response.setContentType("text/plain");
				response.getWriter().write(e.getMessage());
			}
		} else if (source.equalsIgnoreCase("registerBabySitter")) {
			try {
				BabySitter babySitter = new BabySitter();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));

				String data = "";
				if (bufferedReader != null) {
					data = bufferedReader.readLine();
				}

				babySitter = new Gson().fromJson(data, BabySitter.class);

				long bsId = service.registerBabySitter(babySitter);

				response.setContentType("text/plain");

				if (bsId != -1) {
					response.getWriter().write("Baby sitter registered successfully with id: " + bsId);
				} else {
					response.setStatus(400);
					response.getWriter().write("Baby sitter registration failed");
				}
			} catch (SQLException e) {
				response.setStatus(503);
				response.getWriter().write(e.getMessage());
			}
		} else if (source.equalsIgnoreCase("registerToddler")) {
			try {
				Toddler toddler = new Toddler();

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));

				String data = "";
				if (bufferedReader != null) {
					data = bufferedReader.readLine();
				}

				toddler = new Gson().fromJson(data, Toddler.class);

				long tdId = service.tagToddler(toddler);

				if (tdId != -1) {
					response.getWriter().write("Toddler registered successfully with id: " + tdId);
				} else {
					response.setStatus(400);
					response.getWriter().write("Toddler registration failed");
				}
			} catch (SQLException e) {
				response.setStatus(503);
				response.getWriter().write(e.getMessage());
			}
		} else if (source.equalsIgnoreCase("getAllToddlers")) {
			try {
				ArrayList<Toddler> toddlers = service.getAllToddlers();

				String toddlersJson = new Gson().toJson(toddlers);

				response.setContentType("application/json");

				response.getWriter().write(toddlersJson);
			} catch (SQLException e) {
				response.setStatus(503);
				response.getWriter().write(e.getMessage());
			}
		} else if (source.equalsIgnoreCase("getToddler")) {
			try {
				long tdId = Long.parseLong(request.getParameter("tdId"));

				Toddler toddler = service.getToddler(tdId);

				String toddlerJson = new Gson().toJson(toddler);

				response.setContentType("application/json");
				response.getWriter().write(toddlerJson);
			} catch (SQLException e) {
				response.setStatus(503);
				response.getWriter().write(e.getMessage());
			}
		} else if (source.equalsIgnoreCase("updateToddler")) {
			try {
				Toddler toddler = new Toddler();

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));

				String data = "";
				if (bufferedReader != null) {
					data = bufferedReader.readLine();
				}

				toddler = new Gson().fromJson(data, Toddler.class);

				boolean isSuccess = service.updateToddler(toddler);

				response.setContentType("text/plain");

				if (isSuccess) {
					response.getWriter().write("Toddler updated successfully with name: " + toddler.getToodlerName());
				} else {
					response.setStatus(400);
					response.getWriter().write("Toddler updation failed of toddler with id: " + toddler.getToddlerId());
				}
			} catch (SQLException e) {
				response.setStatus(503);
				response.getWriter().write(e.getMessage());
			}
		}
	}

}
