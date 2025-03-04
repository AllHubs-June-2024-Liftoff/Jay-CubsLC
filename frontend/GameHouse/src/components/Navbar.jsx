/* eslint react/prop-types: 0 */
import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router";
import axios from "axios";
import { logOutUser } from "../services/APIservice";
import { userPage } from "../services/APIservice";

function Nav(props) {
  const [theme, setTheme] = useState(
    localStorage.getItem("theme") ? localStorage.getItem("theme") : "light"
  );
  const username = localStorage.getItem("username");
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState("");

  const defaultPic =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0IGztaTnh0lfC-HfbBGq_62Q47LFbLePQjMk1jgEZgBcgwVgkE9CzPQAb-NXECLkWrHQ&usqp=CAU";
  const [user, setUser] = useState(null);
  const handleTheme = (e) => {
    if (e.target.checked) {
      setTheme("dark");
    } else {
      setTheme("light");
    }
  };

  // set theme state in localstorage on mount & also update localstorage on state change
  useEffect(() => {
    localStorage.setItem("theme", theme);
    const localTheme = localStorage.getItem("theme");
    document.documentElement.setAttribute("data-theme", localTheme);

    const fetchUserData = async () => {
      try {
        const response = await userPage(username);
        setUser(response.data);

        setErrorMessage("");
      } catch (error) {
        setErrorMessage(`An error occurred: ${error.message}`);
      }
    };
    fetchUserData();
  }, [theme, username]);

  const handleSearch = (e) => {
    props.setSearchValue(e.target.value);
    navigate("/");
  };
  //   handle logout to clear localStorage.

  const handleLogout = async () => {
    try {
      const response = await logOutUser({
        withCredentials: true,
      });
      localStorage.removeItem("username");
      alert("You have been logged out.");
      navigate(0);
      navigate("/");
      window.location.reload();
    } catch (error) {
      console.error("Error logging out:", error);
      alert("Failed to log out. Please try again.");
    }
  };

  const handleSignIn = () => {
    navigate("/welcome");
  };
  const handleRegister = () => {
    navigate("/register");
  };

  return (
    <div className="bg-base-200  mx-auto flex justify-center relative">
      <div className="navbar w-4/5 mx-5">
        <div className="flex-auto">
          <a href="/" className="btn btn-ghost text-xl font-mono">
            GameHouse
          </a>
        </div>
        <div className="flex-auto gap-2">
          <div className="form-control w-full">
            <input
              type="text"
              value={props.value}
              placeholder="Search"
              className="input input-bordered w-24 md:w-auto rounded-full"
              onChange={handleSearch}
            />
          </div>
        </div>
        <div className="flex-auto justify-end ">
          <button className="btn btn-square btn-ghost ">
            <label className="swap swap-rotate w-12 h-12">
              <input
                type="checkbox"
                onChange={handleTheme}
                checked={theme === "light" ? false : true}
              />
              <svg
                className="swap-on h-7 w-7 fill-current"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
              >
                <path d="M5.64,17l-.71.71a1,1,0,0,0,0,1.41,1,1,0,0,0,1.41,0l.71-.71A1,1,0,0,0,5.64,17ZM5,12a1,1,0,0,0-1-1H3a1,1,0,0,0,0,2H4A1,1,0,0,0,5,12Zm7-7a1,1,0,0,0,1-1V3a1,1,0,0,0-2,0V4A1,1,0,0,0,12,5ZM5.64,7.05a1,1,0,0,0,.7.29,1,1,0,0,0,.71-.29,1,1,0,0,0,0-1.41l-.71-.71A1,1,0,0,0,4.93,6.34Zm12,.29a1,1,0,0,0,.7-.29l.71-.71a1,1,0,1,0-1.41-1.41L17,5.64a1,1,0,0,0,0,1.41A1,1,0,0,0,17.66,7.34ZM21,11H20a1,1,0,0,0,0,2h1a1,1,0,0,0,0-2Zm-9,8a1,1,0,0,0-1,1v1a1,1,0,0,0,2,0V20A1,1,0,0,0,12,19ZM18.36,17A1,1,0,0,0,17,18.36l.71.71a1,1,0,0,0,1.41,0,1,1,0,0,0,0-1.41ZM12,6.5A5.5,5.5,0,1,0,17.5,12,5.51,5.51,0,0,0,12,6.5Zm0,9A3.5,3.5,0,1,1,15.5,12,3.5,3.5,0,0,1,12,15.5Z" />
              </svg>
              <svg
                className="swap-off h-7 w-7 fill-current"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
              >
                <path d="M21.64,13a1,1,0,0,0-1.05-.14,8.05,8.05,0,0,1-3.37.73A8.15,8.15,0,0,1,9.08,5.49a8.59,8.59,0,0,1,.25-2A1,1,0,0,0,8,2.36,10.14,10.14,0,1,0,22,14.05,1,1,0,0,0,21.64,13Zm-9.5,6.69A8.14,8.14,0,0,1,7.08,5.22v.27A10.15,10.15,0,0,0,17.22,15.63a9.79,9.79,0,0,0,2.1-.22A8.11,8.11,0,0,1,12.14,19.73Z" />
              </svg>
            </label>
          </button>

          {user ? (
            <div className="dropdown dropdown-end">
              <div
                tabIndex={0}
                role="button"
                className="btn btn-ghost btn-circle avatar"
              >
                <div className="w-10 rounded-full">
                  <img
                    alt="Profile Picture"
                    src={
                      user.profileImage
                        ? "http://localhost:8080/image/" + username
                        : defaultPic
                    }
                  />
                </div>
              </div>

              <ul
                tabIndex={0}
                className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow"
              >
                <li>
                  <Link to={`/profile/${username}`} className="justify-between">
                    Profile
                  </Link>
                </li>
                <li>
                  <Link to={"/deleteaccount"}>Delete Account</Link>
                </li>

                <li>
                  <button onClick={handleLogout}>Sign Out</button>
                </li>
              </ul>
            </div>
          ) : (
            <div className="flex gap-2">
              <button
                type="submit"
                className="btn btn-primary btn-sm"
                onClick={handleSignIn}
              >
                Sign In
              </button>
              <button
                type="submit"
                className="btn btn-primary btn-sm"
                onClick={handleRegister}
              >
                Register
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Nav;
