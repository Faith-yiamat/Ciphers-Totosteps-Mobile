package com.akirachix.totosteps.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.akirachix.totosteps.R

class AllActivities : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_activities)

    }


    class UserStateManager(context: Context) {
        private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("UserState", Context.MODE_PRIVATE)

        fun setOnboardingComplete() {
            sharedPreferences.edit().putBoolean("onboardingComplete", true).apply()
        }

        fun isOnboardingComplete(): Boolean {
            return sharedPreferences.getBoolean("onboardingComplete", false)
        }

        fun setLoggedIn(isLoggedIn: Boolean) {
            sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
        }

        fun isLoggedIn(): Boolean {
            return sharedPreferences.getBoolean("isLoggedIn", false)
        }
    }

    class MainActivity : AppCompatActivity() {
        private lateinit var userStateManager: UserStateManager

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            userStateManager = UserStateManager(this)

            // Simulate a delay for the splash screen
            android.os.Handler().postDelayed({
                if (userStateManager.isLoggedIn() && userStateManager.isOnboardingComplete()) {
                    startActivity(Intent(this, HomeScreenActivity::class.java))
                } else {
                    startActivity(Intent(this, TeaserOneActivity::class.java))
                }
                finish()
            }, 2000) // 2 seconds delay
        }
    }


    class TeaserOneActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.one_teaser)

            findViewById<Button>(R.id.btnGetStarted).setOnClickListener {
                startActivity(Intent(this, TeaserTwoActivity::class.java))
            }
        }
    }

    class TeaserTwoActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_teaser_two)

            findViewById<Button>(R.id.btnContinue).setOnClickListener {
                startActivity(Intent(this, TeaserThreeActivity::class.java))
            }
        }
    }


    class TeaserThreeActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_teaser_three)

            findViewById<Button>(R.id.btnContinueOne).setOnClickListener {
                startActivity(Intent(this, QuestionScreenOneActivity::class.java))
            }
        }
    }

    class QuestionScreenOneActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_question_screen_one)

            findViewById<Button>(R.id.continueButton).setOnClickListener {
                startActivity(Intent(this, QuestionScreenTwoActivity::class.java))
            }
        }
    }


    class QuestionScreenTwoActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_question_screen_two)

            findViewById<Button>(R.id.submitButton).setOnClickListener {
                startActivity(Intent(this, ViewResultsActivity::class.java))
            }
        }
    }

    class ViewResultsActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_view_results)

            findViewById<Button>(R.id.signInButton).setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    class LoginActivity : AppCompatActivity() {
        private lateinit var userStateManager: UserStateManager

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)
            userStateManager = UserStateManager(this)

            findViewById<Button>(R.id.btnSignUp).setOnClickListener {
                // Perform login logic here
                if (loginSuccessful()) {
                    completeOnboardingAndLogin()
                }
            }
        }

        private fun loginSuccessful(): Boolean {
            // Implement your login logic here
            return true // Replace with actual login check
        }

        private fun completeOnboardingAndLogin() {
            userStateManager.setOnboardingComplete()
            userStateManager.setLoggedIn(true)
            startActivity(Intent(this, HomeScreenActivity::class.java))
            finishAffinity() // This closes all activities in the stack
        }
    }

    class SignupActivity : AppCompatActivity() {
        private lateinit var userStateManager: UserStateManager

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_signup)
            userStateManager = UserStateManager(this)

            findViewById<Button>(R.id.btnSignUp).setOnClickListener {
                // Perform signup logic here
                if (signupSuccessful()) {
                    completeOnboardingAndLogin()
                }
            }
        }

        private fun signupSuccessful(): Boolean {
            // Implement your signup logic here
            return true // Replace with actual signup check
        }

        private fun completeOnboardingAndLogin() {
            userStateManager.setOnboardingComplete()
            userStateManager.setLoggedIn(true)
            startActivity(Intent(this, HomeScreenActivity::class.java))
            finishAffinity() // This closes all activities in the stack
        }
    }

    class HomeScreenActivity : AppCompatActivity() {
        private lateinit var userStateManager: UserStateManager

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_home_screen)
            userStateManager = UserStateManager(this)

            // Implement your main app functionality here
        }

        fun logout() {
            userStateManager.setLoggedIn(false)
            // Optionally, you might want to reset onboarding state too
            // userStateManager.setOnboardingComplete(false)
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity() // This closes all activities in the stack
        }
    }

}