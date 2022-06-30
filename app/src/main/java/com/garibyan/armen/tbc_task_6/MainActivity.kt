package com.garibyan.armen.tbc_task_6

import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.garibyan.armen.tbc_task_6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var usersList: MutableList<User>
    var deletedUsers: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersList = mutableListOf()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            when (true) {
                isEmptyFields() -> {
                    error()
                    Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_SHORT).show()
                }
                !existsByEmail(binding.edtEmail.text.toString()) -> {
                    if (isEmailValid()) {
                        usersList.add(addUser())
                        succes()
                        Toast.makeText(this, R.string.user_successfully_added, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show()
                        binding.edtEmail.error
                    }
                }
                else -> {
                    error()
                    Toast.makeText(this, R.string.user_already_exists, Toast.LENGTH_SHORT).show()
                }
            }
            usersCounter()
            d("MyLog-add", usersList.toString())
        }
        binding.btnDelete.setOnClickListener {
            if (existsByEmail(binding.edtEmail.text.toString())) {
                deleteUser(binding.edtEmail.text.toString())
                succes()
                Toast.makeText(this, R.string.user_successfully_deleted, Toast.LENGTH_SHORT).show()
            } else {
                error()
                Toast.makeText(this, R.string.user_doesnot_exist, Toast.LENGTH_SHORT).show()
            }
            usersCounter()
            d("MyLog-delete", usersList.toString())

        }
        binding.btnUpdate.setOnClickListener {
            if (existsByEmail(binding.edtEmail.text.toString())) {
                updateUser(binding.edtEmail.text.toString())
                succes()
                Toast.makeText(this, R.string.user_successfully_updated, Toast.LENGTH_SHORT).show()
            } else {
                error()
                Toast.makeText(this, R.string.user_doesnot_exist, Toast.LENGTH_SHORT).show()
            }
            d("MyLog-update", usersList.toString())
        }
    }

    private fun addUser(): User = User(
        binding.edtFirstName.text.toString(),
        binding.edtLastName.text.toString(),
        binding.edtAge.text.toString().toInt(),
        binding.edtEmail.text.toString()
    )

    fun deleteUser(email: String) {
        usersList.remove(getUserByEmail(email))
        deletedUsers++
    }

    private fun updateUser(email: String) = with(binding) {
        getUserByEmail(email).firstName = edtFirstName.text.toString()
        getUserByEmail(email).larstName = edtLastName.text.toString()
        getUserByEmail(email).age = edtAge.text.toString().toInt()
    }

    private fun existsByEmail(email: String): Boolean {
        var exists = false
        usersList.forEach {
            exists = it.email == email
        }
        return exists
    }

    private fun usersCounter() = with(binding) {
        tvActiveUsers.text = usersList.size.toString()
        tvDeletedUsers.text = deletedUsers.toString()
    }

    private fun getUserByEmail(email: String): User {
        var user = User()
        usersList.forEach {
            it.email == email
            user = it
        }
        return user
    }

    private fun isEmptyFields(): Boolean = with(binding) {
        return@with edtFirstName.text.toString().isEmpty() ||
                edtLastName.text.toString().isEmpty() ||
                edtAge.text.toString().isEmpty() ||
                edtEmail.text.toString().isEmpty()
    }

    private fun isEmailValid(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text).matches()

    private fun succes() = with(binding) {
        edtFirstName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
        edtLastName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
        edtAge.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
        edtEmail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
    }

    private fun error() = with(binding) {
        edtFirstName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red))
        edtLastName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red))
        edtAge.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red))
        edtEmail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red))
    }
}