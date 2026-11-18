package metweaks.core;

import static metweaks.core.ClassHandler.lastBackwards;
import static metweaks.core.ClassTransformer.coreprefix;
import static metweaks.core.ClassTransformer.log;
import static metweaks.core.ClassTransformer.obf;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TCallsPhysics {
	
	static void transformExplosion(ClassNode classNode) {
		for(MethodNode method : classNode.methods) {
			if(method.name.equals(obf ? "a" : "doExplosionA") && method.desc.equals("()V")) {
				for(AbstractInsnNode node : method.instructions.toArray()) {
					// insert after: hashset.add(new ChunkPosition(j1, k1, l1));
					if(node.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) node).name.equals("add")) {
						InsnList list = new InsnList();
						list.add(new VarInsnNode(ALOAD, 25)); // block
						list.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HooksPhysics.class), "incrementBlock", "(ZLnet/minecraft/block/Block;)V", false));
						// skip the POP instruction to remove the return argument of the "add" call.
						
						method.instructions.remove(node.getNext()); // POP
						method.instructions.insert(node, list);
						log.info(coreprefix+": Patched Explosion.doExplosionA() #1");
						break;
					}
				}
				
				InsnList list = new InsnList();
				list.add(new VarInsnNode(ALOAD, 0)); // explosion
				list.add(new InsnNode(DUP)); // this
				list.add(new FieldInsnNode(GETFIELD, obf ? "agw" :  "net/minecraft/world/Explosion", obf ? "k" : "worldObj" , obf ? "Lahb;" : "Lnet/minecraft/world/World;")); // world
				list.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HooksPhysics.class), "postDetonate", "(Lnet/minecraft/world/Explosion;Lnet/minecraft/world/World;)V", false));
				method.instructions.insertBefore(lastBackwards(method.instructions.getLast(), RETURN), list);
				log.info(coreprefix+": Patched Explosion.doExplosionA() #2");
			}
			else if(method.name.equals(obf ? "a" : "doExplosionB") && method.desc.equals("(Z)V")) {
				for(AbstractInsnNode node : method.instructions.toArray()) {
					// ILOAD 1
					// IFEQ L14
					// INVOKESTATIC
					// IFEQ L14
					if(node.getOpcode() == ILOAD && ((VarInsnNode) node).var == 1 && node.getNext().getOpcode() == IFEQ) { // spawnParticles
						
						
						InsnList list = new InsnList();
						
						list.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HooksPhysics.class), "canSpawnParticles", "()Z", false));
						list.add(new JumpInsnNode(IFEQ, ((JumpInsnNode) node.getNext()).label)); // block
						
						method.instructions.insert(node.getNext(), list);
						log.info(coreprefix+": Patched Explosion.doExplosionB()");
						break;
					}
				}
			}
			
			
		}
		

	}
	
	static void transformWorldServer(ClassNode classNode) {
		
		for(MethodNode method : classNode.methods) {
			if(method.name.equals(obf ? "a" : "newExplosion") && method.desc.equals(obf ? "(Lsa;DDDFZZ)Lagw;" : "(Lnet/minecraft/entity/Entity;DDDFZZ)Lnet/minecraft/world/Explosion;")) {
				for(AbstractInsnNode node : method.instructions.toArray()) {
					// insert after: hashset.add(new ChunkPosition(j1, k1, l1));
					if(node.getOpcode() == ILOAD && ((VarInsnNode) node).var == 10 && node.getNext().getOpcode() == IFNE) { // isSmoking
						// remove until 
						// ALOAD 11 (explosion)
					    // ARETURN
						while(node != null) {
							AbstractInsnNode next = node.getNext();
							if(next.getOpcode() == ARETURN) {
								break;
							}
							method.instructions.remove(node);
							node = next;
						}
						log.info(coreprefix+": Patched WorldServer.newExplosion()");
						return;
					}
				}
			}
		}
		

	}
	
	static void replaceFBConstructors(ClassNode classNode, final int type) {
		String name = 
				type == 0 
					? obf ? "a" : "handleSpawnObject"
					: type == 1
					? "tryBlockFall" :
					obf ? "m" : "func_149830_m";
		
		String desc = 
				type == 0 
					? obf ? "(Lfw;)V" : "(Lnet/minecraft/network/play/server/S0EPacketSpawnObject;)V"
					: type == 1
					? "(Lnet/minecraft/world/World;III)V" :
					obf ? "(Lahb;III)V" : "(Lnet/minecraft/world/World;III)V";
	
		
		
		boolean replaceNEW = true;
		String targetNEW = obf ? "xj" : "net/minecraft/entity/item/EntityFallingBlock";
		String newNEW = "metweaks/physics/PhysicsEntity";
		
		
		for(MethodNode method : classNode.methods) {
			if(method.name.equals(name) && method.desc.equals(desc)) {
				for(AbstractInsnNode node : method.instructions.toArray()) {
					if(replaceNEW && node.getOpcode() == NEW) {
						TypeInsnNode typenode = (TypeInsnNode) node;
						if(typenode.desc.equals(targetNEW)) {
							typenode.desc = newNEW;
							replaceNEW = false;
						}
					}
					else if(node.getOpcode() == INVOKESPECIAL) {
						MethodInsnNode methodnode = (MethodInsnNode) node;
						if(methodnode.name.equals("<init>") && methodnode.owner.equals(targetNEW)) {
							methodnode.owner = newNEW;
							methodnode.desc = "(Lnet/minecraft/world/World;DDDLnet/minecraft/block/Block;I)V"; // replace because obfuscation difference
							log.info(coreprefix+": Patched "+classNode.name+"."+name+"()");
							return;
						}
					}
				}
			}
		}

	}
}
