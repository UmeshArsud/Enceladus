import React from "react";
import "./RegisterUser.css"


const RegisterUser: React.FC = () => {
    return(
        <>
            <div>
                <div className="top-div">
                    <h1 className="name-logo">Enceladus</h1>
                    <div className="header-option">
                        <div className="header-op">👤</div>
                        <div className="header-op">👽</div>
                        <div className="header-op">🔍</div>
                    </div>
                </div>
                <div className="register-main-div">
                    <div className="register-sub-div">
                        <div className="left-side-div">
                            <div>Hello, Enceladus User...</div>
                            <div>Have A Good Day</div>
                            <div>Create Your Account Here</div>
                            <div>And Lets Gets Started...!!!</div>
                            <div></div>
                        </div>
                        <div className="right-side-div">
                            <div className="right-side-sub-div">
                                <div className="main-feild">
                                    <label className="input-lable-feild">First Name</label>
                                    <input 
                                        type="text" 
                                        placeholder="name"
                                        className="input-feild"
                                    />
                                </div>
                                <div className="main-feild">
                                    <label className="input-lable-feild">Last Name</label>
                                    <input 
                                        type="text" 
                                        placeholder="last name"
                                        className="input-feild"
                                    />
                                </div>
                                <div className="main-feild">
                                    <label className="input-lable-feild">username</label>
                                    <input 
                                        type="text" 
                                        placeholder="username"
                                        className="input-feild"
                                    />
                                </div>
                                <div className="main-feild">
                                    <label className="input-lable-feild">e-mail</label>
                                    <input 
                                        type="text"
                                        placeholder="email" 
                                        className="input-feild"
                                    />
                                </div>
                                <div className="main-feild">
                                    <label className="input-lable-feild">Mobile</label>
                                    <input 
                                        type="text" 
                                        placeholder="mobile"
                                        className="input-feild"
                                    />
                                </div>
                                <div className="main-feild">
                                    <label className="input-lable-feild">Password</label>
                                    <input 
                                        type="text" 
                                        placeholder="password"
                                        className="input-feild"
                                    />
                                </div>
                                <div className="main-feild">
                                    <label className="input-lable-feild">Conf. Password</label>
                                    <input 
                                        type="text" 
                                        placeholder="conform your password"
                                        className="input-feild"
                                    />
                                </div>
                            </div>
                        </div>
                     
                    </div>
                </div>
                <div className="bottom-div">
                    <div className="bottom-sub-div">
                        <div className="foot-div">
                            <div className="foot-main">Contacts us</div>
                            <div>mobile no.</div>
                            <div>email</div>
                            <div>tweeter</div>
                            <div>instagram</div>
                        </div>
                        <div className="foot-div">
                            <div className="foot-main">about us</div>
                            <div>admin</div>
                            <div>hr</div>
                            <div>ceo</div>
                            <div>dev</div>
                        </div>
                        <div className="foot-div">
                            <div className="foot-main">more tools</div>
                            <div>Ai</div>
                            <div>Supporter</div>
                            <div>managements</div>
                            <div></div>
                        </div>
                        <div className="foot-div">
                            <div className="foot-main">team</div>
                            <div>ui team</div>
                            <div>test team</div>
                            <div>security team</div>
                            <div>backend team</div>
                        </div>
                    </div>
                    <div className="right">©2026 All Rights Recieved</div>
                </div>
            </div>
        </>
    )
}
export default RegisterUser;



// first name
// last name
// user name
// email
// mobile
// password
// confirm pass