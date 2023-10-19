package br.com.igorbag.githubsearch.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.data.GitHubService
import br.com.igorbag.githubsearch.domain.Repository
import br.com.igorbag.githubsearch.ui.adapter.RepositoryAdapter
import org.json.JSONArray
import org.json.JSONTokener
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

//    lateinit var cardRepository:
    lateinit var nomeUsuario: EditText
    lateinit var btnConfirmar: Button
    lateinit var listaRepositories: RecyclerView
    lateinit var errors: TextView
//    lateinit var repository: Repository
//    lateinit var githubApi: GitHubService
//    lateinit var gitHubApiRetroFit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupListeners()
        setupRetrofit()
        getAllReposByUserName()
    }

    // Metodo responsavel por realizar o setup da view e recuperar os Ids do layout
    fun setupView() {
        //@TODOOk 1 - Recuperar os Id's da tela para a Activity com o findViewById - Ok
        nomeUsuario = findViewById(R.id.et_nome_usuario)
        btnConfirmar = findViewById(R.id.btn_confirmar)
        listaRepositories = findViewById(R.id.rv_lista_repositories)
//        cardRepository = findViewById(R.id.cl_card_content)
        errors = findViewById(R.id.app_error)
    }

    //metodo responsavel por configurar os listeners click da tela
    private fun setupListeners() {
        //@TODOOk 2 - colocar a acao de click do botao confirmar
        btnConfirmar.setOnClickListener {
            saveUserLocal(nomeUsuario.text.toString())
            showUserName()
            errors.setText(showUserName())
            getAllReposByUserName()
        }
    }


    // salvar o usuario preenchido no EditText utilizando uma SharedPreferences
    private fun saveUserLocal(nomeUser: String) {
        //@TODOOk 3 - Persistir o usuario preenchido na editText com a SharedPref no listener do botao salvar
        if(nomeUsuario.text != null) {
            val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString(getString(R.string.nome_usuario), nomeUser)
                apply()
            }
        }
    }

    private fun showUserName(): String? {
        //@TODOOk 4- depois de persistir o usuario exibir sempre as informacoes no EditText  se a sharedpref possuir algum valor, exibir no proprio editText o valor salvo
        var getUserName = ""
        if(nomeUsuario.text != null) {
            getUserName = nomeUsuario.text.toString()
        }
        return getUserName
    }

        //Metodo responsavel por fazer a configuracao base do Retrofit
    fun setupRetrofit(): GitHubService {
            /*
               @TODOOk 5 -  realizar a Configuracao base do retrofit
               Documentacao oficial do retrofit - https://square.github.io/retrofit/
               URL_BASE da API do  GitHub= https://api.github.com/
               lembre-se de utilizar o GsonConverterFactory mostrado no curso
            */
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


            var gitHubApiRetroFit = retrofit.create(GitHubService::class.java)
            return  gitHubApiRetroFit
        }

    //Metodo responsavel por buscar todos os repositorios do usuario fornecido
    fun getAllReposByUserName() {
        // TODOOK 6 - realizar a implementacao do callback do retrofit e chamar o metodo setupAdapter se retornar os dados com sucesso

        var gitHubApiRetroFit: GitHubService = setupRetrofit()

        try {
            gitHubApiRetroFit.getAllRepositoriesByUser(showUserName()!!).enqueue(
                object: Callback<List<Repository>> {
                    override fun onResponse(
                        call: Call<List<Repository>>,
                        response: Response<List<Repository>>
                    ) {
                        errors.text = "entrou pelo menos"
                if (response.isSuccessful) {
                        errors.text = "chegamos no gitHubApiRetroFit real, para pegar dados de api, e deu muito bom"
                    var message = ""
                    try{
                        val repoList = response.body()
                        repoList?.let {
                            setupAdapter(it)
                            repoList.forEach {
                                message += " |-|   ${it.name} - ${it.htmlUrl}\n"
                            }
                        }
                    } catch (ex: Exception) {
                        errors.text = "erros na construção do setupAdapter"
                    }
//                    errors.text = message
                } else {
                    errors.text = "chegamos no gitHubApiRetroFit real, para pegar dados de api, mas deu ruim"
//                    Toast.makeText(applicationContext, R.string.app_error, Toast.LENGTH_LONG).show()
//                    errors.text = "\nError em pegar o response do github API"
                }
                    }

                    override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                        //@TODOOk("Not yet implemented")
                    }
                })
        } catch (ex: Exception) {
            Log.e("Erro" , "Erro ao realizar procedimento")
            errors.text = "Erro, deu ruim na conexão"
        }

    }

    // Metodo responsavel por realizar a configuracao do adapter
    fun setupAdapter(list: List<Repository>) {
        /*
            @TODOOK 7 - Implementar a configuracao do Adapter , construir o adapter e instancia-lo
            passando a listagem dos repositorios
         */

        try {
            val adapter = RepositoryAdapter(list)
            listaRepositories.adapter = adapter
//            adapter.carItemLister = {
//                    repository -> val repositoryUrl = repository
//                errors.text = repositoryUrl.toString()
////            openBrowser(repositoryUrl)
//            }
//            adapter.btnShareLister = {
//                    repository -> val repositoryUrl = repository
////            shareRepositoryLink(repositoryUrl)
//            }
        } catch (ex: Exception) {
            errors.text = "Erros errros no adapter"
        }

    }

    // Metodo responsavel por compartilhar o link do repositorio selecionado
    // @TodoOk 11 - Colocar esse metodo no click do share item do adapter
    fun shareRepositoryLink(urlRepository: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, urlRepository)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    // Metodo responsavel por abrir o browser com o link informado do repositorio

    // @TodoOk 12 - Colocar esse metodo no click item do adapter
    fun openBrowser(urlRepository: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(urlRepository)
            )
        )

    }

}