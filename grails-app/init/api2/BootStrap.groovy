package api2

import groovy.sql.Sql
import org.springframework.transaction.annotation.Transactional

@Transactional
class BootStrap {

    def dataSource
    /*  ---------------------------------- Init Chamando os Demais Procedimentos   ---------------------------------- */
    /*  ---------------------------------- Validando Conexão e Inserindo Registros ---------------------------------- */
    def init = { servletContext ->
        log.info("Iniciando a inicialização do serviço...")
        testarConexaoOracle()
        insereRegistros()
    }
    /*  ---------------------------------- Insere Registros Mockup ---------------------------------- */
    def insereRegistros() {
        def cmd = new Sql(dataSource)
        try {
            log.info("Inserindo Sequences no banco de dados......")
            cmd.execute("BEGIN execute immediate 'DROP SEQUENCE SEQ_ID_REAJUSTE'; execute immediate 'CREATE SEQUENCE SEQ_ID_REAJUSTE INCREMENT BY 1 START WITH 1 MAXVALUE 999 NOCYCLE'; execute immediate 'DROP SEQUENCE SEQ_ID_FUNC'; execute immediate 'CREATE SEQUENCE SEQ_ID_FUNC INCREMENT BY 1 START WITH 1 MAXVALUE 999 NOCYCLE'; execute immediate 'DROP SEQUENCE SEQ_ID_CIDADE'; execute immediate 'CREATE SEQUENCE SEQ_ID_CIDADE INCREMENT BY 1 START WITH 1 MAXVALUE 999 NOCYCLE'; execute immediate 'DROP SEQUENCE SEQ_ID_REAJUSTE'; execute immediate 'CREATE SEQUENCE SEQ_ID_REAJUSTE INCREMENT BY 1 START WITH 1 MAXVALUE 999 NOCYCLE'; END;")
            log.info("Inserindo Registros no banco de dados......")
            cmd.execute("begin insert into cidade(id, nome) values(seq_id_cidade.nextval, 'Sapiranga'); exception when dup_val_on_index then null; end; ")
            cmd.execute("begin insert into cidade(id, nome) values(seq_id_cidade.nextval,'Novo Hamburgo'); exception when dup_val_on_index then null; end; ")
            cmd.execute("begin insert into cidade(id, nome) values(seq_id_cidade.nextval,'Taquara'); exception when dup_val_on_index then null; end; ")
            cmd.execute("begin insert into funcionario(id, cidade_id, nome) values(seq_id_func.nextval, 1, 'Luan Mesquita'); exception when dup_val_on_index then null; end; ")
            cmd.execute("begin insert into funcionario(id, cidade_id,nome) values(seq_id_func.nextval, 2, 'Vitória Marielli'); exception when dup_val_on_index then null; end; ")
            cmd.execute("begin insert into reajuste_salario(id, valor_salario, funcionario_id, data_reajuste) values(seq_id_reajuste.nextval, 3200, 1, trunc(to_date('01/09/2023','dd/mm/yyyy'))); exception when dup_val_on_index then null; end;")
            log.info("Recompilando PACK_DESAFIO_DEV no banco de dados......")
            cmd.execute("begin execute immediate 'ALTER PACKAGE ALUNO9.PACK_DESAFIO_DEV COMPILE BODY'; execute immediate 'ALTER PACKAGE ALUNO9.PACK_DESAFIO_DEV COMPILE'; END;")
            cmd.execute("commit")
        } catch (Exception e) {
            log.info("Erro ao inserir registros:", e)
        } finally {
            cmd.close()
        }
    }
    /*  ---------------------------------- Valida Conexão DataSource ---------------------------------- */
    def testarConexaoOracle() {
        def sql = new Sql(dataSource)
        try {
            sql.execute("SELECT 1 FROM DUAL")
            log.info("Conexão com o banco de dados Oracle bem-sucedida.")
        } catch (Exception e) {
            log.info("Erro ao testar a conexão com o banco de dados Oracle:", e)
        } finally {
            sql.close()
        }
    }
   /*  ---------------------------------- Destroy para Delete de Registros ---------------------------------- */
    def destroy() {
        Cidade.deleteAll()
    }
}