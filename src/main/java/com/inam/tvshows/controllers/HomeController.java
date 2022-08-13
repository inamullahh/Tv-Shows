package com.inam.tvshows.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.inam.tvshows.models.LoginUser;
import com.inam.tvshows.models.TvShow;
import com.inam.tvshows.models.User;
import com.inam.tvshows.services.TvShowService;
import com.inam.tvshows.services.UserService;

@Controller
public class HomeController {

	private final TvShowService nameServ;
	private final UserService userServ;

	public HomeController(UserService userServ, TvShowService nameServ) {
		super();
		this.nameServ = nameServ;
		this.userServ = userServ;
	}

	// **********************************
	// ********** Login and Reg *********
	// **********************************
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("newUser", new User());
		model.addAttribute("newLogin", new LoginUser());
		return "index.jsp";
	}

	// Route to register
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser") User newUser,
			BindingResult result, Model model, HttpSession session) {

		// TO-DO Later -- call a register method in the service
		// to do some extra validations and create a new user!

		userServ.register(newUser, result);

		//
		// session.setAttribute("user_name", newUser.getUserName());

		if (result.hasErrors()) {
			// Be sure to send in the empty LoginUser before
			// re-rendering the page.
			model.addAttribute("newLogin", new LoginUser());
			return "index.jsp";
		}

		session.setAttribute("user_name", newUser.getUserName());

		// No errors!
		// TO-DO Later: Store their ID from the DB in session,
		// in other words, log them in.

		Long userID = newUser.getId();

		session.setAttribute("user_id", userID);

		return "redirect:/dashboard";
	}

	// Route to Log in
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin,
			BindingResult result, Model model, HttpSession session) {

		// Add once service is implemented:
		// User user = userServ.login(newLogin, result);
		User user = userServ.login(newLogin, result);

		if (result.hasErrors()) {

			model.addAttribute("newUser", new User());

			return "index.jsp";
		}

		// No errors!
		// TO-DO Later: Store their ID from the DB in session,
		// in other words, log them in.
		session.setAttribute("user_name", user.getUserName());

		session.setAttribute("user_id", user.getId());

		return "redirect:/dashboard";

	}

	// Route to Log out
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	// **********************************
	// ********** Dashboard *************
	// **********************************
	@GetMapping("/dashboard")
	public String dashboard(@ModelAttribute("tvShow") TvShow tvShow, Model model, HttpSession session) {

		// If id is not in session, redirect to log in page.
		// To Stop users from going to routes without logging in.
		if (session.getAttribute("user_id") == null) {

			return "redirect:/";

		} else {

			// Getting the Id of from session
			Long userID = (Long) session.getAttribute("user_id");

			// Find user by Id
			User currentUser = userServ.findOne(userID);

			//
			tvShow.setUser(currentUser);

			// Send our Shows to the JSP using Model model
			model.addAttribute("allNames", nameServ.allNames());

			return "dashboard.jsp";
		}
	}

	// **********************************
	// ******* Creating a new Show ******
	// **********************************
	@GetMapping("/newShow")
	public String newShow(@ModelAttribute("tvShow") TvShow tvShow, Model model, HttpSession session) {

		// If id is not in session, redirect to log in page.
		// To Stop users from going to routes without logging in.
		if (session.getAttribute("user_id") == null) {
			return "redirect:/";
		}

		// model.addAttribute("allNames", nameServ.allNames());

		return "newShow.jsp";
	}

	// Post Route to create a new Show.
	@PostMapping("/processShow")
	public String postingShow(@Valid @ModelAttribute("tvShow") TvShow tvShow, BindingResult result, Model model,
			HttpSession session) {

		if (result.hasErrors()) {
			model.addAttribute("allNames", nameServ.allNames());
			return "newShow.jsp";

		} else {

			// Getting the Id of from session
			Long userID = (Long) session.getAttribute("user_id");

			// Find user by Id
			User currentUser = userServ.findOne(userID);

			// 
			tvShow.setUser(currentUser);

			// Create a new Show Name
			nameServ.create(tvShow);
			return "redirect:/dashboard";
		}

	}

	// **********************************
	// ******* Viewing one Show *********
	// **********************************
	// Route to one show
	@GetMapping("/oneShow/{id}")
	public String oneShow(@PathVariable("id") Long id, Model model, HttpSession session) {

		// If id is not in session, redirect to log in page.
		// To Stop users from going to routes without logging in.
		if (session.getAttribute("user_id") == null) {
			return "redirect:/";
		}

		// Find user by Id
		Long userID = (Long) session.getAttribute("user_id");

		// Calling to find one TV Show by ID
		// Storing show instance into model -- sending it to JSP.
		model.addAttribute("tvShow", nameServ.findOne(id));

		// Storing session ID into model -- sending it to JSP.
		model.addAttribute("userID", userID);

		return "oneShow.jsp";
	}

	// **********************************
	// ******* Deleting a name **********
	// **********************************
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id, HttpSession session) {

		// If id is not in session, redirect to log in page.
		// To Stop users from going to routes without logging in.
		if (session.getAttribute("user_id") == null) {
			return "redirect:/";
		}

		nameServ.delete(id);

		return "redirect:/dashboard";
	}

	// **********************************
	// ******* Editing a name ***********
	// **********************************
	@GetMapping("/updateShow/{id}")
	public String updateShow(@PathVariable("id") Long id, @ModelAttribute("tvShow") TvShow tvShow, Model model,
			HttpSession session) {

		// If id is not in session, redirect to log in page.
		// To Stop users from going to routes without logging in.
		if (session.getAttribute("user_id") == null) {

			return "redirect:/";
		}

		model.addAttribute("tvShow", nameServ.findOne(id));

		return "updateShow.jsp";
	}

	// Process the Post to update show details
	@RequestMapping(value = "/updatingShow/{id}", method = RequestMethod.PUT)
	public String updatingShow(@Valid @ModelAttribute("tvShow") TvShow tvShow, BindingResult result,
			HttpSession session) {

		if (result.hasErrors()) {
			return "updateShow.jsp";

		} else {
			// Getting the Id of from session
			Long userID = (Long) session.getAttribute("user_id");

			// Find user by Id
			User currentUser = userServ.findOne(userID);

			//
			tvShow.setUser(currentUser);
//			tvShow.setUser(tvShow.getUser());
			nameServ.update(tvShow);

			return "redirect:/dashboard";

		}

	}

}
