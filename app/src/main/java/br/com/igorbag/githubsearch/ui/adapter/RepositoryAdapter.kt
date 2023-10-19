package br.com.igorbag.githubsearch.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.domain.Repository

class RepositoryAdapter(private val repositories: List<Repository>) :
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    var repoItemListener: () -> Unit = {}
    var carItemLister: (Repository) -> Unit = {}
    var btnShareLister: (Repository) -> Unit = {}

    // Cria uma nova view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.repository_item, parent, false)
        return ViewHolder(view)
    }

    // Pega o conteudo da view e troca pela informacao de item de uma lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //@TODOOk 8 -  Realizar o bind do viewHolder
        //Exemplo de Bind
        holder.nameRepository.text = repositories[position].name
//        holder.cardRepository.setOnClickListener{
//            carItemLister(repositories[position])
//        }
//        holder.btnShared.setOnClickListener{
//            btnShareLister(repositories[position])
//        }
    }

    // Pega a quantidade de repositorios da lista
    //@TODOOk 9 - realizar a contagem da lista
    override fun getItemCount(): Int = repositories.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //@TODOOk 10 - Implementar o ViewHolder para os repositorios
        //Exemplo:
        val nameRepository: TextView
//        var cardRepository: ConstraintLayout
//        val btnShared: Button

        init {
            view.apply {
                nameRepository = findViewById(R.id.tv_name_repository)
//                cardRepository = findViewById(R.id.cl_card_content)
//                btnShared = findViewById(R.id.btn_shared)
            }

        }
    }

}
