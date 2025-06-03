package br.com.nat.javalingo;

import br.com.nat.javalingo.principal.Principal;
import br.com.nat.javalingo.repository.PalavraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavalingoApplication implements CommandLineRunner {
	@Autowired
	private PalavraRepository palavraRepository;

	public static void main(String[] args) {
		SpringApplication.run(JavalingoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(palavraRepository);
		principal.exibirMenu();
	}
}
